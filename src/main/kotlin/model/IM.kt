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
        "(Interventions Manager) Schedules appointments and records service user progress on accredited programmes"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }
    }

    override fun defineRelationships() {
      Delius.system.uses(system, "pushes active sentence requirements or licence conditions to", "IAPS")
      InterventionTeams.interventionServicesTeam.uses(system, "creates new accredited programmes in")
      InterventionTeams.npsProgrammeManager.uses(system, "schedules accredited programme appointments and tracks service user attendance in")
      InterventionTeams.crcTreatmentManager.uses(system, "schedules accredited programme appointments and tracks service user attendance in")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "interventions-manager-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
