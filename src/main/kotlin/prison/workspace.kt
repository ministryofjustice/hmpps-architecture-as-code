package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.Workspace
import com.structurizr.model.Enterprise
import uk.gov.justice.hmpps.architecture.*
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

fun prisonWorkspace(): Workspace {
  val hmpps_prisons = Enterprise("HM Prison & Probation Service")
  val workspace = Workspace("Prison systems", "Systems related to the confinement of offenders").apply {
    model.setEnterprise(hmpps_prisons)
    setId(55246)
  }

  // The systems we wish to include in this workspace
  val systems = listOf<HMPPSSoftwareSystem>(
    NOMIS,
    OffenderManagementInCustody,
    PrisonerContentHub
  )
  val probationSystems = listOf(
    Delius,
    ProbationPractitioners
  )

  // We start by defining the deployment nodes. Containers are often associated with deployment nodes
  // during model definition, so we front-load this task to make everything available for that step.
  CloudPlatform.defineDeploymentNodes(workspace.model)

  // Define all our models. These are necessary before we start defining relationships
  // between systems
  systems.forEach {
    it.defineModelEntities(workspace.model)
  }
  probationSystems.forEach {
    it.defineModelEntities(workspace.model)
  }

  // TODO: this should be refactored into the approach in this file
  prisonModel(workspace.model)

  systems.forEach {
    it.defineRelationships()
  }
  probationSystems.forEach {
    it.defineRelationships()
  }

  systems.forEach {
    it.defineViews(workspace.views)
  }

  // TODO: this should be refactored into the approach in this file
  prisonViews(workspace.model, workspace.views)

  // Finally, set styles and other top-level config. This could be done at any stage,
  // and isn't dependent on the other steps above
  styles(workspace.views.configuration.styles)

  return workspace
}
