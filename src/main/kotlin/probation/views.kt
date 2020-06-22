package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

fun probationViews(model: Model, views: ViewSet) {
  views.createSystemLandscapeView("everything", "Absolutely everything").apply {
    addAllElements()
    enableAutomaticLayout()
  }

  val epf = model.getSoftwareSystemWithName("EPF")!!
  views.createSystemContextView(epf, "epfcontext", null).apply {
    addNearestNeighbours(epf)
    addNearestNeighbours(model.getPersonWithName("EPF Product Manager")!!)
    enableAutomaticLayout()
  }

  val im = model.getSoftwareSystemWithName("IM")!!
  views.createSystemContextView(im, "interventionsmanagercontext", null).apply {
    addNearestNeighbours(im)
    enableAutomaticLayout()
  }

  addDelius(model.getSoftwareSystemWithName("nDelius")!!, views)

  val ndmis = model.getSoftwareSystemWithName("NDMIS")!!
  views.createSystemContextView(ndmis, "ndmiscontext", null).apply {
    addNearestNeighbours(ndmis)
    enableAutomaticLayout()
  }
}

private fun addDelius(delius: SoftwareSystem, views: ViewSet) {
  views.createSystemContextView(delius, "deliusdatacontext", null).apply {
    addNearestNeighbours(delius)
    model.people.forEach(this::remove)
    enableAutomaticLayout()
  }

  views.createSystemContextView(delius, "deliuspeoplecontext", null).apply {
    addNearestNeighbours(delius)
    model.softwareSystems.filter { s -> s != delius }.forEach(this::remove)
    enableAutomaticLayout()
  }
}
