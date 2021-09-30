package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class CaseNotesToProbation private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Case Notes to Probation",
        "Polls for case notes and pushes them to probation systems"
      )
    }

    override fun defineRelationships() {
      system.uses(Delius.system, "pushes case notes to")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
