package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ManagePOMCases private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ldu: Person
    lateinit var pom: Person
    lateinit var homd: Person
    lateinit var allocationManager: Container
    lateinit var redis: Container
    lateinit var activeJob: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "Manage POM Cases",
        "A service for handling the handover of offenders from prison to probation"
      )

      ldu = model.addPerson(
        "Local Delivery Unit",
        "A Community/Probation team handling offenders who hail from a defined geographical area"
      )

      pom = model.addPerson(
        "Prison Offender Manager (POM)",
        "A prison staff member allocated to the prisoner"
      )

      homd = model.addPerson(
        "Head Of Offender Management Delivery (HOMD)",
        "A senior staff member who manages the prison's POM team"
      )

      allocationManager = system.addContainer(
        "Offender Management Allocation Manager",
        "Main Manage POM Cases (MPC) application component which holds the UI and primary business logic. " +
          "Not all mastered data is currently available from the API. Contact the project team " +
          "if further fields are required.",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/offender-management-allocation-manager")
        CloudPlatform.kubernetes.add(this)
      }

      db = system.addContainer(
        "MPC database",
        "Database holding data mastered by MPC",
        "AWS RDS PostgreSQL"
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

      activeJob = system.addContainer(
        "Queue Processor",
        "asynchronously processes the REDIS queue",
        "Ruby on Rails"
      ).apply {
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {

      pom.uses(system, "uses")
      homd.uses(system, "allocates POMs to offenders")
      homd.interactsWith(pom, "manages")

      allocationManager.uses(
        Delius.communityApi,
        "retrieves information on the prisoner's Community team, sends offender POM allocations",
        "REST"
      )

      allocationManager.uses(
        NOMIS.prisonApi,
        "retrieves prisoner information for prisons assigned to the logged in user",
        "REST"
      )

      allocationManager.uses(
        Delius.offenderSearch,
        "retrieves TIER & Mappa details for prisoners",
        "REST"
      )

      allocationManager.uses(
        NOMIS.offenderSearch,
        "retrieves the 'recall' status of offenders",
        "REST"
      )

      allocationManager.uses(HMPPSAuth.system, "authenticates users via")

      allocationManager.uses(
        db,
        "stores mastered information",
        "JDBC"
      )

      allocationManager.uses(
        redis,
        "places jobs on the queue"
      )

      activeJob.uses(
        redis,
        "consumes and processes the queue"
      )

      activeJob.delivers(
        ldu,
        "notifies community allocation requests to",
        "gov.uk notify"
      )
    }

    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(
        system,
        "manage-POM-cases-context",
        "Context overview of the Manage POM Cases service"
      ).apply {
        addDefaultElements()
        addNearestNeighbours(softwareSystem)
        enableAutomaticLayout()
      }

      views.createDeploymentView(
        system,
        "manage-POM-cases-deployment",
        "Deployment overview of the Manage POM Cases service"
      ).apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout()
      }

      views.createContainerView(
        system,
        "manage-POM-cases-container",
        "Container overview of the Manage POM Cases Service"
      ).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
