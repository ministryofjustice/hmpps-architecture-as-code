package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Element

enum class Capability : addTo {
  IDENTITY;

  override fun addTo(element: Element) {
    element.addProperty("capability", this.toString())
  }

  fun isOn(element: Element): Boolean {
    return element.properties.get("capability") == this.toString()
  }
}
