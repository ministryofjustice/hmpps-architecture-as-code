package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class HMPPSAudit private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container
    lateinit var database: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("HMPPS Audit", "Stores audit records when business interactions occur")

      app = system.addContainer(
        "HMPPS Audit",
        "HMPPS Audit service",
        "Spring Boot + Kotlin"
      ).apply {
        Tags.REUSABLE_COMPONENT.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        url = "https://github.com/ministryofjustice/hmpps-audit"
      }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "hmpps-audit-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }
    }
  }
}
