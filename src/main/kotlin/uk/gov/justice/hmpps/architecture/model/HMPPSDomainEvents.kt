package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class HMPPSDomainEvents private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var topic: SoftwareSystem

    override fun defineModelEntities(model: Model) {

      topic = model.addSoftwareSystem(
        "hmpps-domain-events",
        "Domain-wide Event Topic implemented with AWS Simple Notification Service"
      )
        .apply {
          Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
          Tags.TOPIC.addTo(this)
        }
    }

    override fun defineRelationships() {}
    override fun defineViews(views: ViewSet) {}
  }
}
