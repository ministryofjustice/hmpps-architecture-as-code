package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.view.PaperSize
import com.structurizr.view.ViewSet

fun prisonViews(model: Model, views: ViewSet) {
    views.createSystemLandscapeView("everything", "Absolutely everything").apply {
        addDefaultElements()
        paperSize = PaperSize.A3_Landscape
    }

    val nomis = model.getSoftwareSystemWithName("NOMIS")!!
    views.createContainerView(nomis, "nomiscontainer", null).apply {
        addDefaultElements()
        enableAutomaticLayout()
    }

    val pathfinder = model.getSoftwareSystemWithName("Pathfinder")!!
    views.createContainerView(pathfinder, "pathfinderContainer", "The container diagram for the Pathfinder System.").apply {
        addDefaultElements()
        add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("NOMIS database"))
        add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("Elite2API"))
        add(model.getSoftwareSystemWithName("NOMIS")?.getContainerWithName("PrisonerSearch"))
        add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("nDelius database"))
        add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("CommunityAPI"))
        add(model.getSoftwareSystemWithName("nDelius")?.getContainerWithName("OffenderSearch"))

        add(model.getSoftwareSystemWithName("HMPPS Auth")!!)
        add(model.getSoftwareSystemWithName("AWS")?.getContainerWithName("ElasticSearch"))

        paperSize = PaperSize.A3_Landscape
        externalSoftwareSystemBoundariesVisible = true
    }

    val systemContextView = views.createSystemContextView(
            pathfinder,
            "PathfinderSystemContext",
            "The system context diagram for the Pathfinder System."
    )
    systemContextView.isEnterpriseBoundaryVisible = false
    systemContextView.paperSize = PaperSize.A3_Landscape
    systemContextView.addDefaultElements()
    systemContextView.add(model.getSoftwareSystemWithName("NOMIS")!!)
    systemContextView.add(model.getSoftwareSystemWithName("nDelius")!!)
    systemContextView.add(model.getSoftwareSystemWithName("HMPPS Auth")!!)
    systemContextView.add(model.getSoftwareSystemWithName("AWS")!!)

}
