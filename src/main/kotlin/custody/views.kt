package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.model.Model
import com.structurizr.view.ViewSet

fun custodyViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("everything", "Absolutely everything").apply {
    addAllElements()
    enableAutomaticLayout()
  }

  val nomis = model.getSoftwareSystemWithName("NOMIS")!!
  views.createContainerView(nomis, "nomiscontainer", null).apply {
    addNearestNeighbours(nomis)
    enableAutomaticLayout()
  }
}
