package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class NDH(model: Model) {
  val system: SoftwareSystem

  init {
    system = model.addSoftwareSystem(
      "NDH",
      "NOMIS Data Hub,\nresponsible for pulling/pushing data between HMPPS case management systems"
    ).apply {
      uses(NOMIS.db, "to search for offenders")
    }
  }
}
