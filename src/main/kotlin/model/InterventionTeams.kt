package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class InterventionTeams private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var interventionServicesTeam: Person
    lateinit var npsTreatmentManager: Person
    lateinit var contractManagerForCRC: Person

    lateinit var crcTreatmentManager: Person
    lateinit var crcProgrammeManager: Person

    lateinit var crsProvider: Person

    override fun defineModelEntities(model: Model) {
      interventionServicesTeam = model.addPerson(
        "Intervention Services Team",
        "They accredit intervention programmes and do business development of the interventions"
      )
      npsTreatmentManager = model.addPerson("NPS treatment manager")
      contractManagerForCRC = model.addPerson("Contract Manager for CRCs")

      crcTreatmentManager = model.addPerson("CRC treatment manager")
        .apply { Tags.PROVIDER.addTo(this) }

      crcProgrammeManager = model.addPerson(
        "CRC programme manager",
        "People who provide interventions on behalf of Community Rehabilitation Companies"
      ).apply { Tags.PROVIDER.addTo(this) }

      crsProvider = model.addPerson(
        "Commissioned rehabilitative services (CRS) provider",
        "Contracted providers delivering rehabilitation and resettlement interventions for service users"
      ).apply {
        Tags.PROVIDER.addTo(this)
      }

      crcProgrammeManager.interactsWith(contractManagerForCRC, "sends rate card brochure to")
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
