package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet

class InterventionTeams private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var interventionServicesTeam: Person
    lateinit var npsProgrammeManager: Person
    lateinit var crcTreatmentManager: Person

    override fun defineModelEntities(model: Model) {
      interventionServicesTeam = model.addPerson(
        "Intervention Services Team",
        "They accredit intervention programmes and do business development of the interventions"
      )
      npsProgrammeManager = model.addPerson("NPS programme manager")
      crcTreatmentManager = model.addPerson("CRC treatment manager")
        .apply { Tags.PROVIDER.addTo(this) }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
