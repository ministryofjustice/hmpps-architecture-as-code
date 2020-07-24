package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

fun probationViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("system-overview", "All systems in HM Probations").apply {
    addAllSoftwareSystems()
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
  }

  views.createSystemLandscapeView(
    "getting-the-right-rehabilitation",
    "Landscape view of the 'getting the right rehabilitation' problem area"
  ).apply {
    val interventionSystems = model.softwareSystems.filter { ProblemArea.GETTING_THE_RIGHT_REHABILITATION.isOn(it) }
    interventionSystems.forEach { addNearestNeighbours(it) }

    val otherSystems = getElements().map { it.element }
      .filterIsInstance<SoftwareSystem>()
      .filterNot { ProblemArea.GETTING_THE_RIGHT_REHABILITATION.isOn(it) }
    otherSystems.forEach { remove(it) }

    setEnterpriseBoundaryVisible(false)
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 400, 400)
  }
}
