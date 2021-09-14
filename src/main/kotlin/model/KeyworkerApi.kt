package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class KeyworkerApi private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Keyworker API",
        "Provides and creates information about keyworkers of prisoners"
      )

      api = system.addContainer("API", "API", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/keyworker-api"
      }

      val db = system.addContainer("Database", "Storage for keyworker information", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      api.uses(db, "connects to")
    }

    override fun defineRelationships() {
      api.uses(HMPPSAuth.app, "validates API tokens via", "JWK")
      api.uses(NOMIS.prisonApi, "retrieves and creates keyworker information in")
      api.uses(ComplexityOfNeed.mainApp, "retrieves complexity of needs information from")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
