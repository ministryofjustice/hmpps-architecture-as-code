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

  val spo = model.addPerson("Senior Prison Offender Manager", "manages service users and offender managers")
  val pom = model.addPerson("Prison Offender Manager", "responsible for the service users in their prison")
  pom.interactsWith(spo, "managed by")

  HmmpsAuth(model)
  Delius(model)
  NDH(model).system
  PATHFINDER(model)

  OffenderManagementInCustody(model).apply {
    spo.uses(allocationManager, "look at unallocated service users coming from court in")
    pom.uses(allocationManager, "look at service users who need handing over to community in")
    allocationManager.uses(NOMIS.prisonApi, "polls service users currently in the logged in user's prison from")
  }
}
