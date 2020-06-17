package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.Workspace

fun probationWorkspace(): Workspace {
  val workspace = Workspace("Probation systems", "Systems related to the probation of offenders")
  workspace.id = 54669

  probationModel(workspace.model)
  probationViews(workspace.model, workspace.views)
  probationStyles(workspace.views.configuration.styles)

  return workspace
}
