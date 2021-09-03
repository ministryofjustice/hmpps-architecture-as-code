package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy
import com.structurizr.model.Enterprise
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.view.ViewSet

private val MODEL_ITEMS = listOf(
  AnalyticalPlatform,
  AssessRisksAndNeeds,
  AzureADTenantJusticeUK,
  BookVideoLink,
  CaseNotesToProbation,
  ComplexityOfNeed,
  CourtUsers,
  CourtRegister,
  CRCSystem,
  Curious,
  Delius,
  DigitalPrisonsNetwork,
  EPF,
  EQuiP,
  HMPPSAuth,
  HMPPSDomainEvents,
  IM,
  Interventions,
  InterventionTeams,
  Licences,
  MoJSignOn,
  NationalPrisonRadio,
  NDH,
  NID,
  NOMIS,
  OASys,
  ManagePOMCases,
  ManageASupervision,
  PolicyTeams,
  PrisonerMoney,
  PrepareCaseForSentence,
  PrisonerContentHub,
  PrisonToProbationUpdate,
  PrisonVisitsBooking,
  ProbationCaseSampler,
  ProbationPractitioners,
  ProbationTeamsService,
  Reporting,
  TierService,
  TokenVerificationApi,
  UserPreferenceApi,
  WhereaboutsApi,
  WMT
)

private fun defineModelItems(model: Model) {
  model.setImpliedRelationshipsStrategy(
    CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy()
  )

  AWS.defineDeploymentNodes(model)
  CloudPlatform.defineDeploymentNodes(model)
  Heroku.defineDeploymentNodes(model)
  Azure.defineDeploymentNodes(model)

  MODEL_ITEMS.forEach { it.defineModelEntities(model) }
  defineModelWithDeprecatedSyntax(model)
}

private fun changeUndefinedLocationsToInternal(model: Model) {
  model.softwareSystems
    .filter { it.location == Location.Unspecified }.forEach { it.setLocation(Location.Internal) }
  model.people
    .filter { it.location == Location.Unspecified }.forEach { it.setLocation(Location.Internal) }
}

private fun defineRelationships() {
  MODEL_ITEMS.forEach { it.defineRelationships() }
}

private fun defineViews(model: Model, views: ViewSet) {
  MODEL_ITEMS.forEach { it.defineViews(views) }
  defineGlobalViews(model, views)
}

fun defineWorkspace(): Workspace {
  val enterprise = Enterprise("HM Prison and Probation Service")
  val workspace = Workspace(enterprise.name, "Systems related to the custody and probation of offenders")
  workspace.id = 56937
  workspace.model.enterprise = enterprise

  defineModelItems(workspace.model)
  changeUndefinedLocationsToInternal(workspace.model)

  defineRelationships()
  defineViews(workspace.model, workspace.views)
  defineStyles(workspace.views.configuration.styles)
  defineDocumentation(workspace)
  pullADRs(workspace)

  return workspace
}
