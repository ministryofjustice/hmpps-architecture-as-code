package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class AssessRisksAndNeeds private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var riskAssessmentUi: Container
    lateinit var assessmentService: Container
    lateinit var riskNeedsService: Container
    lateinit var collector: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Assess Risks And Needs",
        "Digital Service for ongoing offender risk and needs assessments, " +
          "gathering offender risks and needs information, " +
          "calculating risk scores, showing changes over time"
      )

      val assessmentDb = system.addContainer(
        "Assessments Database",
        "Holds assessment questionnaire (pre-sentence, ROSH, etc), and offender assessment data",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      val assessRiskNeedsDb = system.addContainer(
        "Risks and Needs Database",
        "Holds offender risks and needs data, Authoritative source for supplementary risk data",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      assessmentService = system.addContainer(
        "Assessment Service",
        "Assessments business logic, providing REST API consumed by Risk Assessment UI web application, Authoritative source for assessment data",
        "Kotlin + Spring Boot"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        uses(assessmentDb, "connects to", "JDBC")
        url = "https://github.com/ministryofjustice/hmpps-assessments-api"
        CloudPlatform.kubernetes.add(this)
        CloudPlatform.elasticache.add(this)
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
        CloudPlatform.elasticache.add(this)
      }

      riskNeedsService = system.addContainer(
        "Risks and Needs Service",
        "Risks and Needs business logic, Authoritative source for risk and needs data for offenders",
        "Kotlin + Spring Boot"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        uses(assessRiskNeedsDb, "connects to", "JDBC")
        url = "https://github.com/ministryofjustice/hmpps-assess-risks-and-needs"
        CloudPlatform.kubernetes.add(this)
      }

      collector = system.addContainer(
        "ARN data collector",
        "Collects daily snapshots of risk data for hand-off to S3 landing buckets for reporting or analytics",
        "data-engineering-data-extractor"
      ).apply {
        uses(assessRiskNeedsDb, "reads snapshots of the ARN data from")
        Tags.REUSABLE_COMPONENT.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      listOf(riskAssessmentUi, assessmentService, riskNeedsService)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }

      assessmentService.uses(Delius.communityApi, "Gets offender and offence details from")
      assessmentService.uses(PrepareCaseForSentence.courtCaseService, "Gets offender and offence details from")
      assessmentService.uses(OASys.assessmentsApi, "get offender past assessment details from")
      assessmentService.uses(OASys.assessmentsUpdateApi, "pushes offender assessment details into")
      riskNeedsService.uses(OASys.assessmentsApi, "get offender risk and needs data from")
      riskNeedsService.uses(HMPPSDomainEvents.topic, "publishes risk domain events to", "SNS")
      ProbationPractitioners.nps.uses(riskAssessmentUi, "records offender risks and needs")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "assess-risks-and-needs-context", null
      ).apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 500, 500)
      }

      views.createDeploymentView(system, "assess-risks-and-needs-deployment", "Deployment overview of the assess risks and needs services").apply {
        add(AWS.london)
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
