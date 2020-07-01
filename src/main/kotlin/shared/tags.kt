package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.model.Element
import com.structurizr.model.Relationship

enum class Tags() {
  DATABASE, WEB_BROWSER, EXTERNAL, DEPRECATED, SOFTWARE_AS_A_SERVICE, COMMUNITY;

  // Usage example: Tags.DATABASE.add(container)
  fun addTo(element: Element) {
    element.addTags(this.toString())
  }

  fun addTo(relationship: Relationship) {
    relationship.addTags(this.toString())
  }
}
