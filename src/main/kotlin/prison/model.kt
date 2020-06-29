package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  val nomis = NOMIS(model)
  val ndh = NDH(model).system
  val pathfinder = Pathfinder(model)

  ndh.uses(nomis.db, "extract offender data")
  pathfinder.webApp.uses(nomis.elite2api, "extract offender data")
}
