package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model

fun prisonModel(model: Model) {
  val nomis = NOMIS(model).system
  val ndh = NDH(model).system
  val pathfinder = Pathfinder(model).system

  ndh.uses(nomis, "extract offender data")
  pathfinder.uses(nomis, "retrieves offender data via Elite2Api")
}
