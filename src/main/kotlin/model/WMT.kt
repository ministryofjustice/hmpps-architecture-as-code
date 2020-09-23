package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class WMT private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Workload Measurement Tool",
        "(WMT) Helps probation practitioners schedule their time based on service user risk"
      )
    }

    override fun defineRelationships() {
      ProbationPractitioners.nps.uses(system, "finds out their community case load by looking at")
      system.uses(Reporting.ndmis, "draws offender risk and allocation data from")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
