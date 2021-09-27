package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class PrisonToProbationUpdate private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Prison to Probation Update",
        "Listens for events from Prison systems (NOMIS) to update offender sentence information in Probation systems (Delius)"
      ).apply {
        setUrl("https://github.com/ministryofjustice/prison-to-probation-update")
      }
    }

    override fun defineRelationships() {
      system.uses(Delius.system, "update offender sentence information in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
