package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.view.ViewSet

fun prisonViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("everything", "Absolutely everything").apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

  val nomis = model.getSoftwareSystemWithName("NOMIS")!!
  views.createContainerView(nomis, "nomiscontainer", null).apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

  val prisonerContentHub = model.getSoftwareSystemWithName("Prisoner Content Hub")!!
  views.createContainerView(prisonerContentHub, "prisonerContentHubContainer", null).apply {
    addDefaultElements()
    enableAutomaticLayout()
  }
}
