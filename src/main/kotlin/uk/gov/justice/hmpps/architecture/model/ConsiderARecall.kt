package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ConsiderARecall private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ppcsCaseWorker: Person
    lateinit var considerARecallUi: Container
    lateinit var considerARecallApi: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "Consider A Recall",
        "A service to help making decisions around recalls. Previously called Make A Recall Decision."
      )

      db = system.addContainer(
        "Consider A Recall API Database",
        "Database to store recalls caseworking info",
        "RDS Postgres DB"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      considerARecallApi = system.addContainer(
        "Consider A Recall API",
        "REST API for the Consider A Recall service",
        "Kotlin Spring Boot App"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        setUrl("https://github.com/ministryofjustice/make-recall-decision-api")
        CloudPlatform.kubernetes.add(this)
      }

      considerARecallUi = system.addContainer(
        "Consider A Recall Web Application",
        "Web application for the Consider A Recall service",
        "Node Express app"
      ).apply {
        setUrl("https://github.com/ministryofjustice/make-recall-decision-ui")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      listOf(considerARecallApi, considerARecallUi)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }
      considerARecallApi.uses(db, "queries", "JDBC")
      considerARecallApi.uses(Delius.offenderSearch, "searches for offender")
      considerARecallApi.uses(Delius.MRDIntegrationService, "retrieves offender information", "REST+HTTP")
      considerARecallApi.uses(AssessRisksAndNeeds.riskNeedsService, "retrieves risk information", "REST+HTTP")
      considerARecallApi.uses(HMPPSDomainEvents.topic, "publishes finalised decisions events to", "SNS")

      considerARecallUi.uses(HMPPSAudit.system, "records user interactions", "HTTPS")
      considerARecallUi.uses(considerARecallApi, "operates on", "HTTPS")
      ProbationPractitioners.nps.uses(considerARecallUi, "Reviews information on offender, records recall decision")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "consider-a-recall-context",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(
        system,
        "consider-a-recall-container",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(ConsiderARecall.system, "consider-a-recall-deployment", "Deployment overview of the Consider A Recall service").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
