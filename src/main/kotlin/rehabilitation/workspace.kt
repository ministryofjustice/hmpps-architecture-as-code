package uk.gov.justice.hmpps.architecture.rehabilitation

import com.structurizr.Workspace

fun rehabilitationWorkspace(): Workspace {
  val workspace = Workspace("Rehabilitation systems", "Systems related to the rehabilitation of offenders")
  workspace.id = 54669

  rehabilitationModel(workspace.model)
  rehabilitationViews(workspace.model, workspace.views)
  rehabilitationStyles(workspace.views.configuration.styles)

  return workspace
}
