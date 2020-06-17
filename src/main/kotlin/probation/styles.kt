package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.view.Shape
import com.structurizr.view.Styles

fun probationStyles(styles: Styles) {
  styles.addElementStyle("Element").color("#ffffff").background("#006699")

  styles.addElementStyle("database").shape(Shape.Cylinder)
  styles.addElementStyle("Person").shape(Shape.Person).background("#0099cc")

  styles.addElementStyle("external").color("#000000").background("#ccff99")
  styles.addElementStyle("deprecated").background("#999999")
}
