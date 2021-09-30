package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Element

@Deprecated("Do not annotate Containers. Instead, please add an API docs badge to the GitHub repo's README")
class APIDocs(private val url: String) {
  fun addTo(element: Element) {
    element.addProperty("api-docs-url", url)
  }

  companion object {
    fun getFrom(element: Element): String? {
      return element.properties["api-docs-url"]
    }
  }
}
