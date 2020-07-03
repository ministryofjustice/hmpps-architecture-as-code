package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.model.Element
import com.structurizr.model.Relationship

enum class Tags() {
  DATABASE, WEB_BROWSER, EXTERNAL, PROVIDER, DEPRECATED, SOFTWARE_AS_A_SERVICE, PRISON_SERVICE;

  // Usage example: Tags.DATABASE.addTo(any_model_element)
  fun addTo(element: Element) {
    element.addTags(this.toString())
  }
}
