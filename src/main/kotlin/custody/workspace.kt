package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.Workspace

fun custodyWorkspace(): Workspace {
  val workspace = Workspace("Custody systems", "Systems related to the confinement of offenders")
  workspace.id = 55246

  custodyModel(workspace.model)
  custodyViews(workspace.model, workspace.views)
  custodyStyles(workspace.views.configuration.styles)

  return workspace
}
