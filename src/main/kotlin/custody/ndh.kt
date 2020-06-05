package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Model

class NDH(model: Model) {
  val system: SoftwareSystem

  init {
    system = model.addSoftwareSystem("NDH", """
    NOMIS Data Hub,
    responsible for pulling/pushing data between HMPPS case management systems
    """.trimIndent())
  }
}
