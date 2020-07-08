package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class HmmpsAuth(model: Model) {
  val system: SoftwareSystem

  init {
    system = model.addSoftwareSystem("HMPPS Auth", "Allows users to login into digital services").apply {
      setLocation(Location.Internal)
    }
  }
}
