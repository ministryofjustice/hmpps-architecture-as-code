package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

fun defineGlobalViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("system-overview", "All systems").apply {
    addAllSoftwareSystems()

    val noisySignOnSystems = listOf(HMPPSAuth.system, MoJSignOn.system)
    noisySignOnSystems.forEach(::remove)

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

    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 400, 400)
  }

  // lifted from prison views
  val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
  views.createContainerView(pathfinder, "pathfinderContainer", "The container diagram for the Pathfinder System.").apply {
    addDefaultElements()
    add(NOMIS.db)
    add(NOMIS.prisonApi)
    add(NOMIS.system.getContainerWithName("ElasticSearch store"))
    add(NOMIS.offenderSearch)
    add(Delius.system.getContainerWithName("nDelius database"))
    add(Delius.communityApi)
    add(Delius.system.getContainerWithName("ElasticSearch store"))
    add(Delius.offenderSearch)
    add(HMPPSAuth.system)
    enableAutomaticLayout()
    externalSoftwareSystemBoundariesVisible = true
  }

  views.createSystemContextView(
    pathfinder,
    "PathfinderSystemContext",
    "The system context diagram for the Pathfinder System."
  ).apply {
    addDefaultElements()
    add(NOMIS.system)
    add(Delius.system)
    add(HMPPSAuth.system)
    enableAutomaticLayout()
  }

  views.createDeploymentView(pathfinder, "PathfinderProductionDeployment", "The Production deployment scenario for the Pathfinder service").apply {
    addDefaultElements()
    enableAutomaticLayout()
  }
}
