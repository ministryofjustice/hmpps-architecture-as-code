package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

fun probationViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("system-overview", "All systems in HM Probations").apply {
    addAllSoftwareSystems()
    enableAutomaticLayout()
  }

  val epf = model.getSoftwareSystemWithName("EPF")!!
  views.createSystemContextView(epf, "epf-context", null).apply {
    addDefaultElements()
    addNearestNeighbours(model.getPersonWithName("EPF Product Manager")!!)
    enableAutomaticLayout()
  }

  val im = model.getSoftwareSystemWithName("IM")!!
  views.createSystemContextView(im, "interventions-manager-context", null).apply {
    addDefaultElements()
    enableAutomaticLayout()
  }

  addDelius(model.getSoftwareSystemWithName("nDelius")!!, views)

  val ndmis = model.getSoftwareSystemWithName("NDMIS")!!
  views.createSystemContextView(ndmis, "ndmis-context", null).apply {
    addDefaultElements()
    enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 200, 200)
  }
}

private fun addDelius(delius: SoftwareSystem, views: ViewSet) {
  views.createSystemContextView(delius, "delius-data-context", null).apply {
    addDefaultElements()
    model.people.forEach(this::remove)
    enableAutomaticLayout()
  }

  views.createSystemContextView(delius, "delius-people-context", null).apply {
    addDefaultElements()
    model.softwareSystems.filter { it != delius }.forEach(this::remove)
    enableAutomaticLayout()
  }
}
