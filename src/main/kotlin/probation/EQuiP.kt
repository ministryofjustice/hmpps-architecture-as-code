package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class EQuiP private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "EQuiP",
        "Central repository for all step-by-step business processes (in probation?)"
      )
    }

    override fun defineRelationships() {
      ProbationPractitioners.crc.uses(system, "finds information about a process or software in")
      ProbationPractitioners.nps.uses(system, "finds information about a process or software in")
      ProbationPractitioners.nps.uses(system, "finds rate cards in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
