package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Notifier
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Interventions private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ui: Container
    lateinit var service: Container
    lateinit var database: Container
    lateinit var dataCollector: Container
    lateinit var misCollector: Container
    lateinit var deliusEventListener: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Refer and monitor an intervention",
        "Refer and monitor an intervention for people on probation"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      service = system.addContainer(
        "Intervention service (REST API)",
        "Domain API for tracking the lifecycle of CRS (Commissioned Rehabilitative Services) interventions and services, " +
          "including finding, referring, delivering and monitoring",
        "Kotlin + Spring Boot"
      ).apply {
        url = "https://github.com/ministryofjustice/hmpps-interventions-service"
        CloudPlatform.kubernetes.add(this)
      }

      ui = system.addContainer(
        "Intervention UI",
        "Responsible for curating and delivering published interventions and services",
        "Node + Express"
      ).apply {
        url = "https://github.com/ministryofjustice/hmpps-interventions-ui"
        uses(service, "implements intervention processes via")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }

      deliusEventListener = system.addContainer(
        "Interventions-to-Delius event listener",
        "Subscribes to intervention domain events and deals with its consequences for nDelius, eg. NSIs",
        "Kotlin + hmpps-sqs-spring-boot-starter"
      ).apply {
        url = "https://github.com/ministryofjustice/hmpps-delius-interventions-event-listener"
        Tags.PLANNED.addTo(this) // not in production yet
        CloudPlatform.kubernetes.add(this)
      }

      database = system.addContainer(
        "Intervention database",
        "Authoritative source for CRS (Commissioned Rehabilitative Services) interventions, their service categories, " +
          "complexity levels, CRS providers and subcontractors.",
        "PostgreSQL"
      ).apply {
        service.uses(this, "connects to", "JDBC")
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      dataCollector = system.addContainer(
        "Analytical platform data collector",
        "Collects daily snapshots of intervention data for hand-off to S3 landing buckets for the analytical platform",
        "cronjob with data-engineering-data-extractor"
      ).apply {
        uses(database, "reads snapshots of the intervention data from")
        Tags.REUSABLE_COMPONENT.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }

      misCollector = system.addContainer(
        "MIS data collector",
        "Generates daily interventions report for reporting",
        "cronjob with spring-batch, running the service container"
      ).apply {
        uses(database, "reads intervention data from")
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      defineAuthentication()
      defineSharing()
      defineUsers()
    }

    private fun defineAuthentication() {
      InterventionTeams.crsProvider.uses(HMPPSAuth.app, "log in via", "HTTPS/web")
      ProbationPractitioners.nps.uses(HMPPSAuth.app, "log in via", "HTTPS/web")

      ui.uses(HMPPSAuth.app, "requests access tokens from", "OAuth2/JWT")
      service.uses(HMPPSAuth.app, "authorises users and requests access tokens from", "OAuth2/JWT")
    }

    private fun defineSharing() {
      ui.uses(Delius.communityApi, "retrieves current service user profile, appointments and sentence details from", "REST/HTTP")
      ui.uses(AssessRisksAndNeeds.riskNeedsService, "retrieves service user current risks from, stores supplementary risk information in", "REST/HTTP")

      service.uses(HMPPSDomainEvents.topic, "publishes intervention domain events to", "SNS")
      service.uses(Delius.communityApi, "books and reschedules appointments with", "REST/HTTP")
      service.uses(Delius.communityApi, "creates activities (NSI), notifications of progress, records appointment outcomes with", "Spring Application Events+REST/HTTP")

      // remove "records appointment outcomes" from service once this is live
      deliusEventListener.uses(Delius.communityApi, "records appointment outcomes with", "REST/HTTP")
      deliusEventListener.uses(HMPPSDomainEvents.topic, "subscribes to intervention domain events", "SQS")

      misCollector.uses(Reporting.landingBucket, "pushes intervention reports daily to")
      dataCollector.uses(AnalyticalPlatform.landingBucket, "pushes intervention data daily to")
    }

    private fun defineUsers() {
      InterventionTeams.crsProvider.uses(ui, "maintains directory and delivery of dynamic framework interventions and services in")
      ProbationPractitioners.nps.uses(ui, "refers and monitors progress of their service users' interventions and services in")

      Notifier.delivers(
        service,
        listOf(
          Triple(listOf(InterventionTeams.crsProvider), "emails new referrals", "email"),
          Triple(listOf(ProbationPractitioners.nps), "emails attendance and safeguarding issues", "email")
        )
      )
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "interventions-context", "Context overview of the digital intervention services").apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 500, 500)
      }

      views.createDeploymentView(system, "interventions-deployment", "Deployment overview of the digital intervention services").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "interventions-container", "Container overview of the digital intervention services").apply {
        addDefaultElements()
        system.containers.forEach(::addNearestNeighbours)

        // relationships between the dependencies are not important for this view
        // would be nice to have `removeRelationshipsNotConnectedTo(system.containers)`
        // or the ability to set a default for relationships between imported neighbour elements
        HMPPSAuth.app.getEfferentRelationshipsWith(Delius.communityApi).forEach(::remove)

        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 250, 150)
      }
    }
  }
}
