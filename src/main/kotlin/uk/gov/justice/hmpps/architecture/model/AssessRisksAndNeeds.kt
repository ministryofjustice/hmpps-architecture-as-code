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
    lateinit var riskNeedsService: Container
    lateinit var collector: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Assess Risks And Needs API",
        "API Service for exposing risk and needs information to other digital services"
      )

      val assessRiskNeedsDb = system.addContainer(
        "Assess Risks and Needs Database",
        "Holds offender risks and needs data, Authoritative source for supplementary risk data",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      riskNeedsService = system.addContainer(
        "Assess Risks and Needs API",
        "Source of risk and needs data for offenders",
        "Kotlin + Spring Boot"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        uses(assessRiskNeedsDb, "connects to", "JDBC")
        url = "https://github.com/ministryofjustice/hmpps-assess-risks-and-needs"
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      riskNeedsService.uses(HMPPSAuth.system, "authenticates via")
      riskNeedsService.uses(Delius.communityApi, "performs check when requesting data on LAOs")
      riskNeedsService.uses(OASys.assessmentsApi, "get offender past assessment details from")
      riskNeedsService.uses(OASys.assessmentsApi, "get offender risk and needs data from")
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
