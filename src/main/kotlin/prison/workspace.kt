package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.Workspace

import uk.gov.justice.hmpps.architecture.shared.CloudPlatform
import uk.gov.justice.hmpps.architecture.shared.styles

fun prisonWorkspace(): Workspace {
  val workspace = Workspace("Prison systems", "Systems related to the confinement of offenders")
  workspace.id = 55246

  CloudPlatform.defineDeploymentNodes(workspace.model)

  // val systems = listOf<SOMETHING>(
  NOMIS.defineModelEntities(workspace.model)
  PrisonerContentHub.defineModelEntities(workspace.model)
  // )
  
  prisonModel(workspace.model)
  
  // Then we can iterate through the list and gereate relationships and views
  // systems.forEach {
  //   it.defineRelationships()
  // }
  NOMIS.defineRelationships()
  PrisonerContentHub.defineRelationships()

  // systems.forEach {
  //   it.defineViews(workspace.views)
  // }
  NOMIS.defineViews(workspace.views)
  PrisonerContentHub.defineViews(workspace.views)

  prisonViews(workspace.model, workspace.views)
  styles(workspace.views.configuration.styles)

  return workspace
}