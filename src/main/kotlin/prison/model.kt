package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(
      CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  val nomis = NOMIS(model)

  HmmpsAuth(model)
  DELIUS(model)
  NDH(model).system
  PATHFINDER(model)

  PrisonerContentHub(model).apply {
    contentHubFrontend.uses(nomis.elite2api, "lookup visits, canteen, etc.")
  }

}
