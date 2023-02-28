package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class WMT private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ui: Container
    lateinit var batchProcessor: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Workload Measurement Tool",
        "(WMT) Helps probation practitioners schedule their time based on service user risk"
      )

      ui = system.addContainer("WMT Web", "View workload data", "Node").apply {
        url = "https://github.com/ministryofjustice/wmt-web"
      }

      batchProcessor = system.addContainer("WMT Worker", "Overnight processing of NART report extract", "Node").apply {
        url = "https://github.com/ministryofjustice/wmt-worker"
      }

      val db = system.addContainer("Database", "Storage for workload data", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      ui.uses(db, "connects to")
      batchProcessor.uses(db, "connects to")
    }

    override fun defineRelationships() {
      ProbationPractitioners.nps.uses(system, "finds out their community case load by looking at")
      system.uses(Reporting.ndmis, "draws offender risk and allocation data from")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "wmt-context", "Context overview of WMT").apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 500, 500)
      }
    }
  }
}
