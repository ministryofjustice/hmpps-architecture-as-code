package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class NDH private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "NOMIS Data Hub",
        "(NDH) Responsible for pulling/pushing data between HMPPS case management systems"
      )
    }

    override fun defineRelationships() {
      system.uses(NOMIS.db, "to search for offenders")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
