package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.APIDocs
import uk.gov.justice.hmpps.architecture.annotations.Tags

class HMPPSAuth private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("HMPPS Auth", "Allows users to login into digital services")

      app = system.addContainer(
        "API",
        "OAuth2 server integrating with NOMIS database, nDelius (via community api) and an auth database for storing external users",
        "Spring Boot + Java"
      ).apply {
        APIDocs("https://sign-in-preprod.hmpps.service.justice.gov.uk/auth/swagger-ui.html").addTo(this)
        setUrl("https://github.com/ministryofjustice/hmpps-auth")
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
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
