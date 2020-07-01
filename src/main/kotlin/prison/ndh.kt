package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class NDH(model: Model) {
  val system: SoftwareSystem

  init {
    system = model.addSoftwareSystem("NDH", """
    NOMIS Data Hub,
    responsible for pulling/pushing data between HMPPS case management systems
    """.trimIndent()).apply {
      uses(model.getSoftwareSystemWithName("NOMIS")!!.getContainerWithName("NOMIS database")!!,
          "to search for offenders")
    }
  }
}
