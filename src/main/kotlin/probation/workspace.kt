package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.Workspace
import com.structurizr.model.Enterprise
import uk.gov.justice.hmpps.architecture.shared.CloudPlatform
import uk.gov.justice.hmpps.architecture.shared.styles

fun probationWorkspace(): Workspace {
  val workspace = Workspace("Probation systems", "Systems related to the probation of offenders")
  workspace.id = 54669
  workspace.model.enterprise = Enterprise("Probation in HM Prison and Probation Service")

  CloudPlatform.defineDeploymentNodes(workspace.model)
  Delius(workspace.model)
  probationModel(workspace.model)
  probationViews(workspace.model, workspace.views)
  styles(workspace.views.configuration.styles)

  return workspace
}
