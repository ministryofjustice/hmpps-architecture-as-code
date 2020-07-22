package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class EPF private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var projectManager: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "EPF",
        "Effective Proposal Framework\nPresents sentencing options to NPS staff in court who are providing sentencing advice to sentencers"
      )

      projectManager = model.addPerson("EPF Product Manager", "Product manager for the Effective Proposals Framework tool")
    }

    override fun defineRelationships() {
      projectManager.uses(system, "update intervention eligibility for accredited programmes in")
      projectManager.uses(system, "updates interventions table for discretionary services in")
      ProbationPractitioners.nps.uses(system, "enters court, location, offender needs, assessment score data to receive a shortlist of recommended interventions for Pre-Sentence Report Proposal from")
      ProbationPractitioners.nps.uses(system, "enters location, offender needs, assessment score data to receive recommended interventions for licence condition planning from")
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
