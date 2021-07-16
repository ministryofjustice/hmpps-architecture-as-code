package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ComplexityOfNeed private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var microservice: SoftwareSystem
    lateinit var mainApp: Container
    lateinit var redis: Container
    lateinit var activeJob: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {

      microservice = model.addSoftwareSystem(
        "Complexity of Need Microservice",
        "A microservice for Complexity of Need Levels"
      )

      mainApp = microservice.addContainer(
        "Complexity of Need Microservice",
        "Main application component which holds the primary business logic. " +
          "Exposes a REST API for mastered data.",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/hmpps-complexity-of-need")
        CloudPlatform.kubernetes.add(this)
      }

      db = microservice.addContainer(
        "CoN database",
        "Persistent storage of mastered data items and ancilliary data required for processing",
        "AWS RDS PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      redis = microservice.addContainer(
        "In-memory data store",
        "handles processing queues for event distribution",
        "REDIS"
      ).apply {
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.elasticache.add(this)
      }

      activeJob = microservice.addContainer(
        "Queue Processor",
        "asynchronously processes the REDIS queue",
        "Ruby on Rails"
      ).apply {
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {

      mainApp.uses(HMPPSAuth.system, "authenticates access using")

      mainApp.uses(
        db,
        "stores mastered information",
        "JDBC"
      )

      mainApp.uses(
        redis,
        "places jobs on the queue"
      )

      activeJob.uses(
        redis,
        "consumes and processes the queue"
      )

      activeJob.uses(
        HMPPSDomainEvents.topic,
        "post events for other services to consume"
      )
    }

    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(
        microservice,
        "complexity-of-need-context",
        "Context overview of the Complexity of Need microservice"
      ).apply {
        addDefaultElements()
        addNearestNeighbours(softwareSystem)
        enableAutomaticLayout()
      }

      views.createDeploymentView(
        microservice,
        "complexity-of-need-deployment",
        "Deployment overview of the Complexity of Need microservice"
      ).apply {
        add(AWS.london)
        removeRelationshipsNotConnectedToElement(microservice)
        enableAutomaticLayout()
      }

      views.createContainerView(
        microservice,
        "complexity-of-need-container",
        "Container overview of the Complexity of Need microservice"
      ).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
