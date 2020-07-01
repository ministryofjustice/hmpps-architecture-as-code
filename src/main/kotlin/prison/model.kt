package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

fun prisonModel(model: Model) {
  model.setImpliedRelationshipsStrategy(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

  val nomis = NOMIS(model)

  val ndh = NDH(model).system
  ndh.uses(nomis.db, "extract offender data")

  PrisonerContentHub(model).apply {
    contentHubFrontend.uses(nomis.elite2api, "lookup visits, canteen, etc.")
  }
}
