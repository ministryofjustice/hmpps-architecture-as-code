package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class PrisonVisitsBooking private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    protected lateinit var frontend: Container
    protected lateinit var backend: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Prison Visits Booking",
        "A service for booking a social visit to a prisoner in England or Wales"
      )

      frontend = system.addContainer("Frontend", "Public interface for booking a prison visit", "Ruby on Rails").apply {
        setUrl("https://github.com/ministryofjustice/prison-visits-public")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }

      backend = system.addContainer("Backend", "API for the frontend and admin interface for staff to manage bookings", "Ruby on Rails").apply {
        setUrl("https://github.com/ministryofjustice/prison-visits-2")
        CloudPlatform.kubernetes.add(this)
      }

      val db = system.addContainer("Database", "Bookings database", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      frontend.uses(backend, "books and retrieves bookings from", "HTTP")
      backend.uses(db, "connects to")
    }

    override fun defineRelationships() {
      backend.uses(MoJSignOn.app, "authenticates with", "HTTP")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "prison-visits-booking-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "prison-visits-booking-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
