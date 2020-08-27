package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class CRCSystem private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var integratedSystem: SoftwareSystem
    lateinit var standaloneSystem: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      integratedSystem = model.addSoftwareSystem(
        "Integrated CRC software systems",
        "Various systems used to plan, schedule, manage interventions and bookings"
      )

      standaloneSystem = model.addSoftwareSystem(
        "Standalone CRC software systems",
        "Various systems used to plan, schedule, manage interventions and bookings"
      )

      listOf(integratedSystem, standaloneSystem).forEach {
        Tags.PROVIDER.addTo(it)
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(it)
      }
    }

    override fun defineRelationships() {
      integratedSystem.uses(Delius.system, "(some systems, not all) synchronise data with", "SPG")
      ProbationPractitioners.crc.uses(integratedSystem, "retrieves and stores information related to work done by the CRC in")
      ProbationPractitioners.crc.uses(standaloneSystem, "retrieves and stores information related to work done by the CRC in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
