package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.*
import uk.gov.justice.hmpps.architecture.annotations.Tags

class AssessRisksAndNeeds private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var riskAssessmentUi: Container
    lateinit var assessmentService: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Assess Risks And Needs",
        "Digital Service for ongoing offender risk and needs assessments, "
          + "gathering offender risks and needs information, " +
          "calculating risk scores, showing changes over time"
      )

      val assessmentDb = system.addContainer(
        "ARN Database",
        "Holds assessment questionnaire (pre-sentence, ROSH, etc), and offender assessment data",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      assessmentService = system.addContainer(
        "Assessment Service",
        "Assess Risks and Needs business logic, providing REST API consumed by Risk Assessment UI web application",
        "Kotlin + Spring Boot"
      ).apply {
        uses(assessmentDb, "connects to", "JDBC")
        setUrl("https://github.com/ministryofjustice/hmpps-assessments-api")
      }

      riskAssessmentUi = system.addContainer(
        "Risk Assessment UI",
        "Web application, presenting risk assessment questionnaires",
        "Node + Express"
      ).apply {
        uses(assessmentService, "Display assessment questions and save answers using ")
        setUrl("https://github.com/ministryofjustice/hmpps-risk-assessment-ui")
      }
    }

    override fun defineRelationships() {
      listOf(riskAssessmentUi, assessmentService)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }

      assessmentService.uses(Delius.communityApi, "Gets offender and past-offence details from")
      assessmentService.uses(PrepareCaseForCourt.courtCaseService, "Gets offender and offence details from")
      assessmentService.uses(OASys.assessmentsApi, "get offender past assessment details from")
      assessmentService.uses(OASys.system, "pushes offender assessment details into")

      ProbationPractitioners.nps.uses(riskAssessmentUi, "records offender risks and needs")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system,
      "assess-risks-and-needs-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "assess-risks-and-needs-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
