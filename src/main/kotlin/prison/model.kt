package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(
      CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  HmmpsAuth(model)
  NOMIS(model)
  DELIUS(model)
  NDH(model).system
  PATHFINDER(model)

}
