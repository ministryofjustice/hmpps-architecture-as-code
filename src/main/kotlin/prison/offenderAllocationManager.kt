package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class OffenderAllocationManager(model: Model) {
  val system: SoftwareSystem

  init {
    system = model.addSoftwareSystem("Offender Management Allocation Manager", """
    A service for allocating Prisoners to Prisoner Offender Managers (POMs)
    """.trimIndent())
  }
}
