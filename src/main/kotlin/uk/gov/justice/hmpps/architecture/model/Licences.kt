package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class Licences private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("Licences", "This system is a stub. Please help us expand it.")
    }

    override fun defineRelationships() {
      system.uses(ProbationTeamsService.api, "retrieves and updates probation areas and Local Delivery Unit 'functional' mailboxes in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
