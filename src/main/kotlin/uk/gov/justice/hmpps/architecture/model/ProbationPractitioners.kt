package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class ProbationPractitioners private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var nps: Person
    lateinit var crc: Person

    override fun defineModelEntities(model: Model) {
      nps = model.addPerson(
        "NPS probation practitioner",
        "National Probation Service employed probation officers in custody, court and the community"
      )
      crc = model.addPerson(
        "CRC offender manager",
        "Probation officers in custody, court and the community employed by intervention providers"
      ).apply { Tags.PROVIDER.addTo(this) }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
