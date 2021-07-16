package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.OutsideHMPPS

class Curious private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var DPS: SoftwareSystem
    lateinit var curiousApi: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Curious",
        "(Contract management system for prison education providers"
      ).apply {
        OutsideHMPPS.addTo(this)
        Azure.sequation.add(this)
      }
      curiousApi = system.addContainer(
        "Curious API",
        "API over the Curious application - a contract management system for Education Providers", "Java"
      ).apply {
        url = "https://github.com/ministryofjustice/curious-API"
      }

      DPS = model.addSoftwareSystem(
        "DPS - used by Digital Prison team applications and services",
        "HTTPS"
      ).apply {
        uses(curiousApi, "connects to", "RestHTML")
      }
    }
    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createContainerView(system, "curiouscontainer", null).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }
    }
  }
}
