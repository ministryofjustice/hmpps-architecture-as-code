package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Notifier
import uk.gov.justice.hmpps.architecture.annotations.Tags

class UseOfForce private constructor() {

  companion object : HMPPSSoftwareSystem {
    lateinit var model: Model
    lateinit var system: SoftwareSystem
    lateinit var useOfForceService: Container
    lateinit var db: Container
    lateinit var redis: Container

    lateinit var reportCreator: Person
    lateinit var coordinator: Person
    lateinit var reviewer: Person
    lateinit var involvedStaff: Person

    override fun defineModelEntities(model: Model) {
      this.model = model

      system = model.addSoftwareSystem(
        "Use of Force",
        "A system for allowing prison staff to report a Use of Force instance",
      ).apply {
        val PRISON_PROBATION_PROPERTY_NAME = "business_unit"
        val PRISON_SERVICE = "prisons"
        addProperty(PRISON_PROBATION_PROPERTY_NAME, PRISON_SERVICE)
      }

      db = system.addContainer(
        "Use of Force Database",
        "Database to store Use of Force reports", "RDS Postgres DB"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      redis = system.addContainer(
        "In-memory data store",
        "handles processing queues for email distribution and NOMIS movements",
        "REDIS"
      ).apply {
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.elasticache.add(this)
      }

      useOfForceService = system.addContainer("Use of Force service", "Allows prison staff to report a Use of Force instance", "Node").apply {
        setUrl("https://github.com/ministryofjustice/use-of-force")

        CloudPlatform.kubernetes.add(this)
      }

      reportCreator = model.addPerson("Report creator user", "Prison staff who creates reports and provides statements").apply {
        uses(useOfForceService, "Creates Use of Force reports")
        coordinator = model.addPerson("Coordinator user", "Prison staff who can view all complete and in progress reports and statements across their caseload")
        reviewer = model.addPerson("Reviewer user", "Prison staff who can review and maintain reports across their caseload")
        involvedStaff = model.addPerson("Involved staff user", "Prison staff who provides statements")
      }
    }

    override fun defineRelationships() {
      useOfForceService.uses(HMPPSAuth.system, "HTTPS Rest API")
      useOfForceService.uses(NOMIS.prisonApi, "extract NOMIS offender data")
      useOfForceService.uses(NOMIS.offenderSearch, "to search for prisoners")
      useOfForceService.uses(TokenVerificationApi.api, "validates user tokens via", "HTTPS Rest API")

      Notifier.delivers(
        useOfForceService,
        listOf(
          Triple(
            listOf(involvedStaff),
            "emails notification that staff member has been added to reports and incomplete statement reminders",
            "email"
          ),
        )
      )
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system, "useOfForceSystemContext", "The system context diagram for the Use of Force service"
      ).apply {
        addDefaultElements()

        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createSystemContextView(
        system, "useOfForceUserRelationships", "Relationships between users and the Use of Force service"
      ).apply {
        add(system)
        add(reportCreator)
        add(coordinator)
        add(reviewer)
        add(involvedStaff)

        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "useOfForceContainer", "Use of Force service container view").apply {
        addDefaultElements()
        remove(coordinator)
        remove(reviewer)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(system, "useOfForceContainerProductionDeployment", "The Production deployment scenario for the Use of Force service").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
