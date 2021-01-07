package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ProbationTeamsService private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Probation team contact and reference service",
        "Exposes probation areas and Local Delivery Unit 'functional' mailboxes as an API"
      )

      api = system.addContainer("API", "API", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/probation-teams"
      }

      val db = system.addContainer("Database", "Storage for probation areas and Local Delivery Unit 'functional' mailboxes", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      api.uses(db, "connects to")
    }

    override fun defineRelationships() {
      api.uses(HMPPSAuth.app, "validates API tokens via", "JWK")
    }

    override fun defineViews(views: ViewSet) {
      views.createContainerView(system, "probation-teams-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
