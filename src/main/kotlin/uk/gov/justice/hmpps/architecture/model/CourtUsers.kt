package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

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
