package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class TokenVerificationApi private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Token verification API",
        "Verifies API tokens issues by HMPPS Auth to ensure they haven't expired or been revoked"
      ).apply {
        Tags.AUTH_API.addTo(this)
      }

      api = system.addContainer("API", "API", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/token-verification-api"
      }

      val redis = system.addContainer("REDIS", "Tokens storage", "REDIS").apply {
        Tags.DATABASE.addTo(this)
      }

      api.uses(redis, "connects to")
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
