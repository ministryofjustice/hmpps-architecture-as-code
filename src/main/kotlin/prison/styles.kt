package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.view.Shape
import com.structurizr.view.Styles

const val DATABASE_TAG = "database";

fun prisonStyles(styles: Styles) {
  styles.addElementStyle("Element").color("#ffffff").background("#006699")

  styles.addElementStyle(Tags.DATABASE.toString()).shape(Shape.Cylinder)
  styles.addElementStyle("Person").shape(Shape.Person).background("#0099cc")

  styles.addElementStyle(Tags.WEB_BROWSER.toString()).shape(Shape.WebBrowser)

  styles.addElementStyle(Tags.EXTERNAL.toString()).color("#000000").background("#ccff99")
  styles.addElementStyle(Tags.DEPRECATED.toString()).background("#999999")
}
