package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Element

enum class Tags() {
  DATABASE, WEB_BROWSER, PROVIDER, DEPRECATED, SOFTWARE_AS_A_SERVICE;

  // Usage example: Tags.DATABASE.addTo(any_model_element)
  fun addTo(element: Element) {
    element.addTags(this.toString())
  }
}
