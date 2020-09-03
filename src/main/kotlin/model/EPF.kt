package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea

class EPF private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var projectManager: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "EPF",
        "(Effective Proposal Framework) Presents sentencing options to NPS staff in court who are providing sentencing advice to sentencers"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      projectManager = model.addPerson("EPF Product Manager", "Product manager for the Effective Proposals Framework tool")
    }

    override fun defineRelationships() {
      projectManager.uses(system, "update intervention eligibility for accredited programmes in")
      projectManager.uses(system, "updates interventions table for discretionary services in")
      projectManager.interactsWith(PolicyTeams.sentencingPolicy, "listens to owners of interventions for changes in policy")
      ProbationPractitioners.nps.uses(system, "enters court, location, offender needs, assessment score data to receive a shortlist of recommended interventions for Pre-Sentence Report Proposal from")
      ProbationPractitioners.nps.uses(system, "enters location, offender needs, assessment score data to receive recommended interventions for licence condition planning from")
      InterventionTeams.interventionServicesTeam.interactsWith(projectManager, "provide programme suitability guide for accredited programme eligibility to")
      InterventionTeams.contractManagerForCRC.interactsWith(projectManager, "sends a signed off version of the CRC's Discretionary Services rate card brochure to")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "epf-context", null).apply {
        addDefaultElements()
        addNearestNeighbours(projectManager)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 500, 500)
      }
    }
  }
}
