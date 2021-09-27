package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Element

enum class Tags : addTo {
  DATABASE,
  TOPIC,
  QUEUE,
  WEB_BROWSER,
  REUSABLE_COMPONENT,
  PROVIDER {
    override fun addTo(element: Element) {
      super.addTo(element)
      OutsideHMPPS.addTo(element)
    }
  },
  PLANNED,
  DEPRECATED,
  SOFTWARE_AS_A_SERVICE;

  // Usage example: Tags.DATABASE.addTo(any_model_element)
  override fun addTo(element: Element) {
    element.addTags(this.toString())
  }
}
