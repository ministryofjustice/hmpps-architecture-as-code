package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.model.Enterprise

fun defineWorkspace(): Workspace {
  val enterprise = Enterprise("HM Prison and Probation Service")
  val workspace = Workspace(enterprise.name, "Systems related to the custody and probation of offenders")
  workspace.id = 56937
  workspace.model.enterprise = enterprise

  CloudPlatform.defineDeploymentNodes(workspace.model)

  val modelItems = listOf(
    CRCSystem, 
    Delius, 
    EPF, 
    EQuiP, 
    IM, 
    NationalPrisonRadio,
    NID, 
    NOMIS, 
    OffenderManagementInCustody, 
    PrisonerContentHub,
    ProbationPractitioners, 
    Reporting
  )
  modelItems.forEach { it.defineModelEntities(workspace.model) }

  defineModelWithDeprecatedSyntax(workspace.model)
  modelItems.forEach { it.defineRelationships() }
  modelItems.forEach { it.defineViews(workspace.views) }

  defineViews(workspace.model, workspace.views)
  defineStyles(workspace.views.configuration.styles)

  return workspace
}
