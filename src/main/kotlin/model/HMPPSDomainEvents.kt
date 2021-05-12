package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class HMPPSDomainEvents private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "hmpps-domain-events",
        "Domain-wide Event Topic implemented with AWS Simple Notification Service"
      )
        .apply {
          Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
          Tags.TOPIC.addTo(this)
        }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(
        system,
        "hmpps-domain-events-context",
        "Context view of hmpps-domain-events topic"
      ).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }

      views.createDeploymentView(
        system,
        "hmpps-domain-events-deployment",
        "Deployment view of hmpps-domain-events topic"
      ).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }

      views.createContainerView(
        system,
        "hmpps-domain-events-container",
        "Container view of hmpps-domain-events topic"
      ).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }
    }
  }
}
