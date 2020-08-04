package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class IM private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "IM",
        "Interventions Manager\nHolds records of interventions delivered to services users in the community"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }
    }

    override fun defineRelationships() {
      Delius.system.uses(system, "pushes active sentence requirements or licence conditions which are of interest to IM to", "IAPS")
      InterventionTeams.interventionServicesTeam.uses(system, "create new interventions in")
      InterventionTeams.npsProgrammeManager.uses(system, "create new interventions in")
      InterventionTeams.crcTreatmentManager.uses(system, "create new interventions in")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "interventions-manager-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
