package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Interventions private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ui: Container
    lateinit var service: Container
    lateinit var database: Container
    lateinit var queue: Container
    lateinit var listener: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Getting the right rehabilitation",
        "Supports maintaning interventions and services and finding, booking, delivering and monitoring interventions (currently only Dynamic Framework)"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
        Tags.PLANNED.addTo(this)
      }

      service = system.addContainer(
        "Intervention service",
        "Tracks the lifecycle of dynamic framework interventions and services, including publishing, finding, referring, delivering and monitoring",
        "Java or Kotlin"
      )

      ui = system.addContainer(
        "Intervention UI",
        "Responsible for curating published interventions and services",
        "node"
      ).apply {
        uses(service, "implements intervention processes via")
        Tags.WEB_BROWSER.addTo(this)
      }

      database = system.addContainer(
        "Intervention database",
        "Authoritative source for dynamic framework interventions, service categories, complexity levels; " +
          "Potential source for dynamic framework providers",
        "likely PostgreSQL"
      ).apply {
        service.uses(this, "connects to", "JDBC")
        Tags.DATABASE.addTo(this)
      }

      queue = system.addContainer(
        "Intervention queue(s)",
        "Queue(s) for notifications about intervention domain events, please see link for details",
        "Amazon Simple Queue Service (SQS)"
      ).apply {
        setUrl("https://dsdmoj.atlassian.net/wiki/spaces/IC/pages/2461827117/Architecture+overview+-+Interventions")
        service.uses(this, "publishes domain events to", "SNS")
        Tags.QUEUE.addTo(this)
        CloudPlatform.sqs.add(this)
      }

      listener = system.addContainer(
        "Intervention-probation translator",
        "Maintains contacts, appointments and registrations based on dynamic framework intervention domain events",
        "undefined"
      ).apply {
        uses(queue, "consumes dynamic framework intervention domain events from")
        CloudPlatform.kubernetes.add(this)
      }

      system.containers.forEach { Tags.PLANNED.addTo(it) }
    }

    override fun defineRelationships() {
      defineAuthentication()
      defineSharing()
      defineUsers()
    }

    fun defineAuthentication() {
      service.uses(HMPPSAuth.app, "authenticates, authorises users and requests access tokens from", "OAuth2/JWT")
    }

    fun defineSharing() {
      service.uses(Delius.communityApi, "retrieves list of dynamic framework providers and probation regions from", "REST/HTTP")
      service.uses(Delius.communityApi, "retrieves current service user appointments from", "REST/HTTP")
      service.uses(Delius.offenderSearch, "searches service user by identity and gets basic identification details from", "REST/HTTP")
      service.uses(OASys.assessmentsApi, "retrieves service user current risks and needs from", "REST/HTTP")

      listener.uses(Delius.communityApi, "maintains contacts, appointments, registrations with", "REST/HTTP")!!
        .apply { addTags(Tags.PLANNED.toString()) }
    }

    fun defineUsers() {
      InterventionTeams.dynamicFrameworkProvider.uses(ui, "maintains directory and delivery of dynamic framework interventions and services in")
      ProbationPractitioners.nps.uses(ui, "refers and monitors progress of their service users' interventions and services in")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "interventions-context", "Context overview of the digital intervention services").apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "interventions-container", "Container overview of the digital intervention services").apply {
        addDefaultElements()
        system.containers.forEach(::addNearestNeighbours)

        // relationships between the dependencies are not important for this view
        // would be nice to have `removeRelationshipsNotConnectedTo(system.containers)`
        // or the ability to set a default for relationships between imported neighbour elements
        HMPPSAuth.app.getEfferentRelationshipsWith(Delius.communityApi).forEach(::remove)

        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
