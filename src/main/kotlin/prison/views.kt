package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.view.PaperSize
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

  val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
  views.createContainerView(pathfinder, "pathfinderContainer", "The container diagram for the Pathfinder System.").apply {
    addDefaultElements()
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("Elite2 API"))
    paperSize = PaperSize.A5_Landscape
    enableAutomaticLayout()
    externalSoftwareSystemBoundariesVisible = true
  }

}
