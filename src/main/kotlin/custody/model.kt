package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.model.Model

fun custodyModel(model: Model) {
  val nomis = NOMIS(model).system
  val ndh = NDH(model).system

  ndh.uses(nomis, "extract offender data")
}
