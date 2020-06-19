package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.view.PaperSize
import com.structurizr.view.ViewSet

fun prisonViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("everything", "Absolutely everything").apply {
    addAllElements()
    enableAutomaticLayout()
  }

  val nomis = model.getSoftwareSystemWithName("NOMIS")!!
  views.createContainerView(nomis, "nomiscontainer", null).apply {
    addNearestNeighbours(nomis)
    enableAutomaticLayout()
  }

  val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
  views.createContainerView(pathfinder, "pathfinderContainer", null).apply {
    addAllPeople()
    addAllContainersAndInfluencers()
    add(nomis)
    paperSize = PaperSize.A5_Landscape
    enableAutomaticLayout()
  }

}
