package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class PolicyTeams private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var sentencingPolicy: Person

    override fun defineModelEntities(model: Model) {
      sentencingPolicy = model.addPerson("Sentencing Policy", "Pseudo-team to capture sentencing policy meeting participants")
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
