package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ProbationAllocationTool private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ui: Container
    lateinit var allocationsApi: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Probation Case Allocation tool",
        "Allows users to allocate probation practitioners to PoPs (People on Probation)"
      )

      ui = system.addContainer("Manage a Workforce UI", "UI for allocating cases", "Node").apply {
        url = "https://github.com/ministryofjustice/manage-a-workforce-ui"
      }

      allocationsApi = system.addContainer("Allocations API", "Provides information related to unallocated cases", "Kotlin").apply {
        url = "https://github.com/ministryofjustice/hmpps-allocations"
      }

      val allocationsDb = system.addContainer("Database", "Storage for current unallocated cases", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      allocationsApi.uses(allocationsDb, "connects to")

      ui.uses(allocationsApi, "connects to")
    }

    override fun defineRelationships() {
      ProbationPractitioners.spo.uses(system, "find and allocates unallocated cases by looking at")
      system.uses(Delius.communityApi, "gets probation case data from")
      system.uses(OASys.system, "gets PoP risk data from")

      ui.uses(WMT.workloadApi, "connects to")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "allocations-context", "Context overview of probation allocation tool").apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 500, 500)
      }
    }
  }
}
