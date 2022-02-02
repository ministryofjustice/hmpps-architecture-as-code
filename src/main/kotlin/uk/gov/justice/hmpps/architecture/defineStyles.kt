package uk.gov.justice.hmpps.architecture

import com.structurizr.view.Border
import com.structurizr.view.Shape
import com.structurizr.view.Styles
import uk.gov.justice.hmpps.architecture.annotations.Tags

val softwareColour = "#250469"
val dbColour = "#1A6926"
val personColour = "#7E68AB"
val pipeColour = "#B56107"
val textColour = "#FFFFFF"
val providerColour = "#683D0F"

fun defineStyles(styles: Styles) {

  styles.addElementStyle("Software System")
    .shape(Shape.RoundedBox)
    .background(softwareColour)
    .color(textColour)

  styles.addElementStyle("Container")
    .shape(Shape.RoundedBox)
    .background(softwareColour)
    .color(textColour)

  styles.addElementStyle("Person")
    .shape(Shape.Person)
    .background(personColour)
    .color(textColour)

  styles.addElementStyle(Tags.DATABASE.toString())
    .shape(Shape.Cylinder)
    .background(dbColour)
    .color(textColour)

  styles.addElementStyle(Tags.TOPIC.toString())
    .shape(Shape.Pipe)
    .background(pipeColour)
    .color(textColour)

  styles.addElementStyle(Tags.QUEUE.toString())
    .shape(Shape.Pipe)
    .background(pipeColour)
    .color(textColour)

  styles.addElementStyle(Tags.WEB_BROWSER.toString()).shape(Shape.WebBrowser)
  styles.addElementStyle(Tags.REUSABLE_COMPONENT.toString()).shape(Shape.Hexagon)
  styles.addElementStyle(Tags.PLANNED.toString()).border(Border.Dotted).opacity(50)

  styles.addElementStyle(Tags.PROVIDER.toString()).background(providerColour)
  styles.addElementStyle(Tags.DEPRECATED.toString()).background("#999999")
}
