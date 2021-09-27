package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class NationalPrisonRadio private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "National Prison Radio",
        "The national radio station for prisoners. Made by prisoners, for prisoners"
      ).apply {
        Tags.PROVIDER.addTo(this)
      }
    }

    override fun defineRelationships() {}

    override fun defineViews(views: ViewSet) {}
  }
}
