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

  val im = model.getSoftwareSystemWithName("IM")!!
  views.createSystemContextView(im, "interventions-manager-context", null).apply {
    addDefaultElements()
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
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
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
  }

  views.createSystemContextView(delius, "delius-people-context", null).apply {
    addDefaultElements()
    model.softwareSystems.filter { it != delius }.forEach(this::remove)
    enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
  }
}
