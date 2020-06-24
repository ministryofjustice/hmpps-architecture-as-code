package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.view.Shape
import com.structurizr.view.Styles

fun styles(styles: Styles) {
  styles.addElementStyle("Software System").color("#ffffff").background("#006699")
  styles.addElementStyle("Person").shape(Shape.Person).background("#0099cc")

  styles.addElementStyle("database").shape(Shape.Cylinder)
  styles.addElementStyle("deprecated").background("#999999")
  styles.addElementStyle("external").color("#000000").background("#ccff99")
  styles.addElementStyle("prison-service").color("#000000").background("#ffdf2d")
  styles.addElementStyle("WebBrowser").shape(Shape.WebBrowser)
}
