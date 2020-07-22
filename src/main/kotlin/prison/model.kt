package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model


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
  NDH(model).system
  PATHFINDER(model)

  spo.uses(OffenderManagementInCustody.allocationManager, "look at unallocated service users coming from court in")
  pom.uses(OffenderManagementInCustody.allocationManager, "look at service users who need handing over to community in")
}
