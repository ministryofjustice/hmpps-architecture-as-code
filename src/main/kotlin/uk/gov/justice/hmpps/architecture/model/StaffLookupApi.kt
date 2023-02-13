package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Capability
import uk.gov.justice.hmpps.architecture.annotations.Tags

class StaffLookupApi private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Staff Lookup Service",
        "stores a copy of e-mail data and information like their first and last name associated with MoJ Staff who have email ending in @justice.gov.uk"
      ).apply {
        Capability.IDENTITY.addTo(this)
      }

      api = system.addContainer("API", "API", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/hmpps-staff-lookup-service"
      }

      val db = system.addContainer("Database", "Storage for staff copy", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      api.uses(db, "connects to")
    }

    override fun defineRelationships() {
      api.uses(HMPPSAuth.app, "validates API tokens via", "JWK")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
