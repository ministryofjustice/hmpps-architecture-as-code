package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.shared.Tags

class CRCSystem private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "CRC software systems",
        "Various systems used to plan, schedule, manage interventions and bookings"
      ).apply {
        Tags.PROVIDER.addTo(this)
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }
    }

    override fun defineRelationships() {
      system.uses(Delius.system, "(some systems, not all) synchronise data with", "SPG")
      ProbationPractitioners.crc.uses(system, "retrieves and stores information related to work done by the CRC in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
