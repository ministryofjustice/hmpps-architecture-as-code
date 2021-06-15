package uk.gov.justice.hmpps.architecture

import com.structurizr.view.Border
import com.structurizr.view.Shape
import com.structurizr.view.Styles
import uk.gov.justice.hmpps.architecture.annotations.Tags

fun defineStyles(styles: Styles) {
  styles.addElementStyle("Software System").background("#aabbdd")
  styles.addElementStyle("Person").shape(Shape.Person).background("#aabbdd")

  styles.addElementStyle(Tags.DATABASE.toString()).shape(Shape.Cylinder)
  styles.addElementStyle(Tags.TOPIC.toString()).shape(Shape.Pipe)
  styles.addElementStyle(Tags.QUEUE.toString()).shape(Shape.Pipe)
  styles.addElementStyle(Tags.WEB_BROWSER.toString()).shape(Shape.WebBrowser)
  styles.addElementStyle(Tags.REUSABLE_COMPONENT.toString()).shape(Shape.Hexagon)
  styles.addElementStyle(Tags.PLANNED.toString()).border(Border.Dotted).opacity(50)

  styles.addElementStyle(Tags.PROVIDER.toString()).background("#ccff99")
  styles.addElementStyle(Tags.DEPRECATED.toString()).background("#999999")
}
