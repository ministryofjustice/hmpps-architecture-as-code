package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

import uk.gov.justice.hmpps.architecture.probation.Delius

fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(
      CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  HmmpsAuth(model)
  Delius(model)
  NDH(model).system
  PATHFINDER(model)

  
}
