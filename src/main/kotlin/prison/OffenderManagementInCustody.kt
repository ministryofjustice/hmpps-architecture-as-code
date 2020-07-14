package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem

class OffenderManagementInCustody(model: Model) {
  val system: SoftwareSystem
  val ldu: Person
  val allocationManager: Container

  init {
    system = model.addSoftwareSystem(
      "Offender Management in Custody",
      "A service for handling the handover of service users from prison to probation"
    ).apply {
      setLocation(Location.Internal)
    }

    ldu = model.addPerson("Local Divisional Unit").apply {
      setLocation(Location.Internal)
    }

    allocationManager = system.addContainer("Offender Management Allocation Manager", "A service for allocating Prisoners to Prisoner Offender Managers (POMs)", "Ruby on Rails").apply {
      setUrl("https://github.com/ministryofjustice/offender-management-allocation-manager")
    }

    ldu.uses(allocationManager, "gets notification about ??? from", "gov.uk notify")
  }
}
