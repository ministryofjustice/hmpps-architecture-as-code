package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class IM private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var i2nTeam: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Interventions Manager",
        "(IM) Used to schedule accredited programmes and record service users' progress on them"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      i2nTeam = model.addPerson(
        "i2n (formerly Northgate)",
        "Provides and maintains the Intervention Manager service"
      ).apply {
        addProperty("previous-name", "Northgate")
        Tags.PROVIDER.addTo(this)
      }
    }

    override fun defineRelationships() {
      Delius.system.uses(system, "pushes active sentence requirements or licence conditions to", "IAPS")
      InterventionTeams.npsTreatmentManager.uses(system, "schedules accredited programme appointments, tracks service user attendance and evaluate service user progress in")
      InterventionTeams.crcTreatmentManager.uses(system, "schedules accredited programme appointments, tracks service user attendance and evaluate service user progress in")

      Delius.supportTeam.interactsWith(i2nTeam, "notify an accredited programme is updated", "email")
      i2nTeam.uses(system, "creates new accredited programmes in")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "interventions-manager-context", null).apply {
        addDefaultElements()
        add(i2nTeam)
        add(Delius.supportTeam)
        add(InterventionTeams.interventionServicesTeam)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
