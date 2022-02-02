package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class AdjudicationsApi private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Adjudications API",
        "Allows reporting and viewing of Adjudications"
      ).apply {
        Tags.PRISONS_API.addTo(this)
      }

      api = system.addContainer("API", "API", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/hmpps-manage-adjudications-api"
      }

      val db = system.addContainer("Database", "Storage for adjudication information", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      api.uses(db, "connects to")
    }

    override fun defineRelationships() {
      api.uses(HMPPSAuth.app, "validates API tokens via", "JWK")
      api.uses(NOMIS.prisonApi, "retrieves and creates adjudication information in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
