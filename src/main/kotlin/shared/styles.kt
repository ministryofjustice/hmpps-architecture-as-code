package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.view.Shape
import com.structurizr.view.Styles

import uk.gov.justice.hmpps.architecture.shared.Tags

fun styles(styles: Styles) {
  styles.addElementStyle("Software System").color("#ffffff").background("#006699")
  styles.addElementStyle("Person").shape(Shape.Person).background("#0099cc")

  styles.addElementStyle(Tags.DATABASE.toString()).shape(Shape.Cylinder)

  styles.addElementStyle(Tags.PRISON_SERVICE.toString()).color("#000000").background("#ffdf2d")

  styles.addElementStyle(Tags.WEB_BROWSER.toString()).shape(Shape.WebBrowser)

  styles.addElementStyle(Tags.EXTERNAL.toString()).color("#000000").background("#ccff99")
  styles.addElementStyle(Tags.DEPRECATED.toString()).background("#999999")
}
