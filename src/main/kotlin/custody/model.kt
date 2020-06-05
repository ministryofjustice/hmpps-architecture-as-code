package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Container
import com.structurizr.model.Model

class CustodyModel(model: Model) {
  init {
    val nomis = NOMIS(model).system
    val ndh = NDH(model).system

    ndh.uses(nomis, "extract offender data")
  }
}
