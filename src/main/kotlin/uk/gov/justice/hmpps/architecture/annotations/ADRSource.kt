package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.SoftwareSystem

class ADRSource(private val url: String) {
  fun addTo(system: SoftwareSystem) {
    system.addProperty("adr-url", url)
  }

  companion object {
    fun getFrom(system: SoftwareSystem): String? {
      return system.properties["adr-url"]
    }
  }
}
