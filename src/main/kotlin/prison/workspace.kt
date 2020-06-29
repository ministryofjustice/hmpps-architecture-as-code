package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.Workspace

fun prisonWorkspace(): Workspace {
  val workspace = Workspace("Prison systems", "Systems related to the confinement of offenders")
  workspace.id = 55246

  prisonModel(workspace.model)
  prisonViews(workspace.model, workspace.views)
  prisonStyles(workspace.views.configuration.styles)
  return workspace
}