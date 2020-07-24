package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Element

enum class ProblemArea() {
  GETTING_THE_RIGHT_REHABILITATION;

  fun addTo(element: Element) {
    element.addProperty("problem-area", this.toString())
  }

  fun isOn(element: Element): Boolean {
    return element.properties.get("problem-area") == this.toString()
  }
}
