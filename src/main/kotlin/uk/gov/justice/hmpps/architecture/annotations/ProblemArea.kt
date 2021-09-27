package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Element

enum class ProblemArea : addTo {
  GETTING_THE_RIGHT_REHABILITATION;

  override fun addTo(element: Element) {
    element.addProperty("problem-area", this.toString())
  }

  fun isOn(element: Element): Boolean {
    return element.properties.get("problem-area") == this.toString()
  }
}
