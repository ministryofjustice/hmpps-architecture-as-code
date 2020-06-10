package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.model.Model
import com.structurizr.view.ViewSet

class CustodyViews(model: Model, views: ViewSet) {
  init {
    views.createSystemLandscapeView("everything", "Absolutely everything").apply {
      addAllElements()
      enableAutomaticLayout()
    }

    views.createContainerView(model.getSoftwareSystemWithName("NOMIS"), "nomiscontainer", null).apply {
      addAllElements()
      enableAutomaticLayout()
    }
  }
}
