package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

import uk.gov.justice.hmpps.architecture.probation.Delius

/**
  * Please don't define models here. New models should be added
  * to workspace.kt following the patterns there.
  *
  * TODO: refactor these models to use the new approach, and remove this file
  */
fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(
    CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  HmmpsAuth(model)
  Delius(model)
  NDH(model).system
  PATHFINDER(model)
}
