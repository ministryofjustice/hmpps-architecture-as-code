package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

fun prisonViews(model: Model, views: ViewSet) {

  views.createSystemLandscapeView("everything", "Absolutely everything").apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

  val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
  views.createContainerView(pathfinder, "pathfinderContainer",
      "The container diagram for the Pathfinder System.").apply {
    addDefaultElements()
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("NOMIS database"))
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("Prison API"))
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("ElasticSearch store"))
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("PrisonerSearch"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("nDelius database"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("Community API"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("ElasticSearch store"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("OffenderSearch"))
    add(model.getSoftwareSystemWithName("HMPPS Auth")!!)
    enableAutomaticLayout()
    externalSoftwareSystemBoundariesVisible = true
  }

  views.createSystemContextView(
      pathfinder,
      "PathfinderSystemContext",
      "The system context diagram for the Pathfinder System."
  ).apply {
    addDefaultElements()
    add(model.getSoftwareSystemWithName("NOMIS")!!)
    add(model.getSoftwareSystemWithName("nDelius")!!)
    add(model.getSoftwareSystemWithName("HMPPS Auth")!!)
    enableAutomaticLayout()
    isEnterpriseBoundaryVisible = false
  }

  views.createDeploymentView(pathfinder, "PathfinderProductionDeployment", "The Production deployment scenario for the Pathfinder service").apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

}
