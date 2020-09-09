package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Element

class APIDocs(url: String) {
  val url: String = url

  fun addTo(element: Element) {
    element.addProperty("api-docs-url", url)
  }
}
