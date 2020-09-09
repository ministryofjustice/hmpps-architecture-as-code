package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class MoJSignOn private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("MoJ SignOn", "Allows users to login into digital services")

      app = system.addContainer("MoJ SignOn Webapp", "OAuth based single sign on component designed to authorise staff access to multiple applications with a single account", "Ruby on Rails").apply {
        setUrl("https://github.com/ministryofjustice/moj-signon")
        Tags.WEB_BROWSER.addTo(this)
        Heroku.dyno.add(this)
      }

      val db = system.addContainer("Database", "Contains authentication and authorisation records", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
        Heroku.database.add(this)
      }

      app.uses(db, "connects to")
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
