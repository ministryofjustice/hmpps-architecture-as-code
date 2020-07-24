package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

fun defineViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("system-overview", "All systems").apply {
    addAllSoftwareSystems()
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
  }

  // lifted from probation views
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

  // lifted from prison views
  val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
  views.createContainerView(pathfinder, "pathfinderContainer", "The container diagram for the Pathfinder System.").apply {
    addDefaultElements()
    add(NOMIS.db)
    add(NOMIS.prisonApi)
    add(NOMIS.system.getContainerWithName("ElasticSearch store"))
    add(NOMIS.system.getContainerWithName("PrisonerSearch"))
    add(Delius.system.getContainerWithName("nDelius database"))
    add(Delius.system.getContainerWithName("Community API"))
    add(Delius.system.getContainerWithName("ElasticSearch store"))
    add(Delius.system.getContainerWithName("OffenderSearch"))
    add(model.getSoftwareSystemWithName("HMPPS Auth")!!)
    enableAutomaticLayout()
    externalSoftwareSystemBoundariesVisible = true
  }

  views.createSystemContextView(
    pathfinder, "PathfinderSystemContext", "The system context diagram for the Pathfinder System."
  ).apply {
    addDefaultElements()
    add(NOMIS.system)
    add(Delius.system)
    add(model.getSoftwareSystemWithName("HMPPS Auth")!!)
    enableAutomaticLayout()
    isEnterpriseBoundaryVisible = false
  }

  views.createDeploymentView(pathfinder, "PathfinderProductionDeployment", "The Production deployment scenario for the Pathfinder service").apply {
    addDefaultElements()
    enableAutomaticLayout()
  }
}
