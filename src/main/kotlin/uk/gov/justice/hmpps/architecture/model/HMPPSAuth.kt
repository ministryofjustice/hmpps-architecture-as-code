package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class HMPPSAuth private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("HMPPS Auth", "Allows users to login into digital services")

      app = system.addContainer(
        "HMPPS Auth",
        "UI and OAuth2 server integrating with NOMIS database, nDelius (via community api) and an auth database for storing external users",
        "Spring Boot + Java"
      ).apply {
        Tags.WEB_BROWSER.addTo(this)
        url = "https://github.com/ministryofjustice/hmpps-auth"
      }

      val database = system.addContainer(
        "Internal Auth Database",
        "Holds explicit credentials, roles, multi-factor settings and banning data",
        "Microsoft SQL Server"
      ).apply {
        Tags.DATABASE.addTo(this)
      }

      app.uses(database, "connects to")
    }

    override fun defineRelationships() {
      app.uses(NOMIS.db, "authenticates via")
      app.uses(Delius.communityApi, "authenticates via")
      app.uses(AzureADTenantJusticeUK.directory, "authenticates via")
      app.uses(TokenVerificationApi.api, "stores tokens in")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "hmpps-auth-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }
    }
  }
}
