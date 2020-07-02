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
  views.createSystemContextView(prisonerContentHub, "prisonerContentHubSystemContext", "The system context diagram for the Prisoner Content Hub"
  ).apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

  views.createContainerView(prisonerContentHub, "prisonerContentHubContainer", null).apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

  views.createDeploymentView(prisonerContentHub, "prisonerContentHubContainerProductionDeployment", "The Production deployment scenario for the Prisoner Content Hub").apply {
    addDefaultElements()
    enableAutomaticLayout()
  }


  val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
  views.createContainerView(pathfinder, "pathfinderContainer",
      "The container diagram for the Pathfinder System.").apply {
    addDefaultElements()
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("NOMIS database"))
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("Elite2 API"))
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("ElasticSearch"))
    add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("PrisonerSearch"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("nDelius database"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("CommunityAPI"))
    add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("ElasticSearch"))
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

}
