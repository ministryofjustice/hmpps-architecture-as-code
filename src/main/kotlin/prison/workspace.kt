package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.Workspace

import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.shared.CloudPlatform
import uk.gov.justice.hmpps.architecture.shared.styles

fun prisonWorkspace(): Workspace {
  val workspace = Workspace("Prison systems", "Systems related to the confinement of offenders")
  workspace.id = 55246

  // The systems we wish to include in this workspace
  val systems = listOf<HMPPSSoftwareSystem>(
    NOMIS,
    PrisonerContentHub
  )

  // We start by defining the deployment nodes. Containers are often associated with deployment nodes
  // during model definition, so we front-load this task to make everything available for that step.
  CloudPlatform.defineDeploymentNodes(workspace.model)
  
  // Define all our models. These are necessary before we start defining relationships
  // between systems
  systems.forEach {
    it.defineModelEntities(workspace.model)
  }

  // TODO: this should be refactored into the approach in this file
  prisonModel(workspace.model)
  
  // Then we can iterate through the list and gereate relationships and views
  systems.forEach {
    it.defineRelationships()
  }

  // We can now define all our views
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