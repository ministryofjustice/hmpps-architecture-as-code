package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class MakeARecallDecision private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ppcsCaseWorker: Person
    lateinit var makeARecallDecisionUi: Container
    lateinit var makeARecallDecisionApi: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "Make A Recall Decision",
        "A service to help making decisions around recalls"
      )

      db = system.addContainer(
        "Make A Recall Decision API Database",
        "Database to store recalls caseworking info",
        "RDS Postgres DB"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      makeARecallDecisionApi = system.addContainer(
        "Make A Recall Decision API",
        "REST API for the Make A Recall Decision service",
        "Kotlin Spring Boot App"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        setUrl("https://github.com/ministryofjustice/make-recall-decision-api")
        CloudPlatform.kubernetes.add(this)
      }

      makeARecallDecisionUi = system.addContainer(
        "Make A Recall Decision Web Application",
        "Web application for the Make A Recall Decision service",
        "Node Express app"
      ).apply {
        setUrl("https://github.com/ministryofjustice/make-recall-decision-ui")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      listOf(makeARecallDecisionApi, makeARecallDecisionUi)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }
      makeARecallDecisionApi.uses(db, "queries", "JDBC")
      makeARecallDecisionApi.uses(Delius.offenderSearch, "searches for offender")
      makeARecallDecisionApi.uses(Delius.MRDIntegrationService, "retrieves offender information", "REST+HTTP")
      makeARecallDecisionApi.uses(AssessRisksAndNeeds.riskNeedsService, "retrieves risk information", "REST+HTTP")
      makeARecallDecisionApi.uses(HMPPSDomainEvents.topic, "publishes finalised decisions events to", "SNS")

      makeARecallDecisionUi.uses(HMPPSAudit.system, "records user interactions", "HTTPS")
      makeARecallDecisionUi.uses(makeARecallDecisionApi, "operates on", "HTTPS")
      ProbationPractitioners.nps.uses(makeARecallDecisionUi, "Reviews information on offender, records recall decision")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "make-a-recall-decision-context",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(
        system,
        "make-a-recall-decision-container",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(MakeARecallDecision.system, "make-a-recall-decision-deployment", "Deployment overview of the make a recall decision service").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
