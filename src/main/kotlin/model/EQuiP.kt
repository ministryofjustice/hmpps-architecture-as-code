package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea

class EQuiP private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "EQuiP",
        "Central repository for all step-by-step business processes and policy documents in probation"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }
    }

    override fun defineRelationships() {
      PolicyTeams.sentencingPolicy.uses(system, "updates policy changes in")
      ProbationPractitioners.crc.uses(system, "finds information about a process or software in")
      ProbationPractitioners.nps.uses(system, "finds information about a process or software in")
      ProbationPractitioners.nps.uses(system, "finds rate cards in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
