package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.model.Tags;
import com.structurizr.view.Shape
import com.structurizr.view.Styles
import com.structurizr.view.ViewSet

const val DATABASE_TAG = "database";

fun prisonViews(model: Model, views: ViewSet) {
  

  /**
   * Add custom styles to differentiate containers, e.g. Person, Database
   */
  val styles = views.getConfiguration().getStyles();
  //styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
  styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
  styles.addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);
  
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
  views.createContainerView(prisonerContentHub, "prisonerContentHubContainer", null).apply {
    addDefaultElements()
    enableAutomaticLayout()
  }
}
