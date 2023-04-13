package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class UnpaidWorkService private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var riskAssessmentUi: Container
    lateinit var assessmentService: Container
    lateinit var collector: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Unpaid Work Assessment Service",
        "Digital Service for assessing a PoP's needs ahead of unpaid work (ie community payback) sessions"
      )

      val assessmentDb = system.addContainer(
        "Assessments Database",
        "Holds unpaid work assessment questionnaire and assessment data",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      assessmentService = system.addContainer(
        "Assessment Service",
        "Assessments business logic, providing REST API consumed by Risk Assessment UI web application, Authoritative source for unpaid work assessment data",
        "Kotlin + Spring Boot"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        uses(assessmentDb, "connects to", "JDBC")
        url = "https://github.com/ministryofjustice/hmpps-assessments-api"
        CloudPlatform.kubernetes.add(this)
      }

      riskAssessmentUi = system.addContainer(
        "Risk Assessment UI",
        "Web application, presenting risk assessment questionnaires",
        "Node + Express"
      ).apply {
        uses(assessmentService, "Display assessment questions and save answers using ")
        url = "https://github.com/ministryofjustice/hmpps-risk-assessment-ui"
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }

    }

    override fun defineRelationships() {
      listOf(riskAssessmentUi, assessmentService)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }

      assessmentService.uses(AssessRisksAndNeeds.riskNeedsService, "Gets risk information from")
      assessmentService.uses(Delius.communityApi, "Gets offender and offence details from")
      assessmentService.uses(PrepareCaseForSentence.courtCaseService, "Gets offender and offence details from")
      assessmentService.uses(OASys.assessmentsApi, "get offender past assessment details from")
      ProbationPractitioners.nps.uses(riskAssessmentUi, "records offender risks and needs")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "unpaid-work-service-context", null
      ).apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 500, 500)
      }

      views.createDeploymentView(system, "unpaid-work-service-deployment", "Deployment overview of the assess risks and needs services").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "unpaid-work-service-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
