package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  val nomis = NOMIS(model)
  val ndh = NDH(model).system

  ndh.uses(nomis.db, "extract offender data")
}
