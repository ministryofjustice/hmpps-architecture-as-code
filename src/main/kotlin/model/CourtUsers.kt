package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet

class CourtUsers private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var courtAdministrator: Person

    override fun defineModelEntities(model: Model) {
      courtAdministrator = model.addPerson("NPS court administrator")
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
