package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class RestrictedPatientsApi private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Restricted Patients API",
        "Provides and creates information about restricted patiente"
      )

      api = system.addContainer("API", "API", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/hmpps-restricted-patients-api"
      }

      val db = system.addContainer("Database", "Storage for restricted patient information", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      api.uses(db, "connects to")
    }

    override fun defineRelationships() {
      api.uses(HMPPSAuth.app, "validates API tokens via", "JWK")
      api.uses(NOMIS.prisonApi, "retrieves and creates restricted patient information in")
      api.uses(NOMIS.offenderSearch, "retrieves prisoner data from")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
