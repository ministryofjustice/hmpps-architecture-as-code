package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.model.Element
import com.structurizr.model.Relationship

enum class Tags() {
  DATABASE, WEB_BROWSER, EXTERNAL, PROVIDER, DEPRECATED, SOFTWARE_AS_A_SERVICE, COMMUNITY, PRISON_SERVICE;

  // Usage example: Tags.DATABASE.addTo(container)
  fun addTo(element: Element) {
    element.addTags(this.toString())
  }

  fun addTo(relationship: Relationship) {
    relationship.addTags(this.toString())
  }
}
