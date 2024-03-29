package uk.gov.justice.hmpps.architecture
import com.structurizr.Workspace
import com.structurizr.model.CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy
import com.structurizr.model.Enterprise
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.model.AWS
import uk.gov.justice.hmpps.architecture.model.AdjudicationsApi
import uk.gov.justice.hmpps.architecture.model.AnalyticalPlatform
import uk.gov.justice.hmpps.architecture.model.AssessRisksAndNeeds
import uk.gov.justice.hmpps.architecture.model.Azure
import uk.gov.justice.hmpps.architecture.model.AzureADTenantJusticeUK
import uk.gov.justice.hmpps.architecture.model.BookVideoLink
import uk.gov.justice.hmpps.architecture.model.CRCSystem
import uk.gov.justice.hmpps.architecture.model.CaseNotesToProbation
import uk.gov.justice.hmpps.architecture.model.CloudPlatform
import uk.gov.justice.hmpps.architecture.model.ComplexityOfNeed
import uk.gov.justice.hmpps.architecture.model.ConsiderARecall
import uk.gov.justice.hmpps.architecture.model.CourtRegister
import uk.gov.justice.hmpps.architecture.model.CourtUsers
import uk.gov.justice.hmpps.architecture.model.CreateAndVaryALicence
import uk.gov.justice.hmpps.architecture.model.Curious
import uk.gov.justice.hmpps.architecture.model.Delius
import uk.gov.justice.hmpps.architecture.model.DigitalPrisonServices
import uk.gov.justice.hmpps.architecture.model.DigitalPrisonsNetwork
import uk.gov.justice.hmpps.architecture.model.EPF
import uk.gov.justice.hmpps.architecture.model.EQuiP
import uk.gov.justice.hmpps.architecture.model.HMPPSAPI
import uk.gov.justice.hmpps.architecture.model.HMPPSAudit
import uk.gov.justice.hmpps.architecture.model.HMPPSAuth
import uk.gov.justice.hmpps.architecture.model.HMPPSDomainEvents
import uk.gov.justice.hmpps.architecture.model.Heroku
import uk.gov.justice.hmpps.architecture.model.IM
import uk.gov.justice.hmpps.architecture.model.InterventionTeams
import uk.gov.justice.hmpps.architecture.model.Interventions
import uk.gov.justice.hmpps.architecture.model.KeyworkerApi
import uk.gov.justice.hmpps.architecture.model.Licences
import uk.gov.justice.hmpps.architecture.model.ManageASupervision
import uk.gov.justice.hmpps.architecture.model.ManagePOMCases
import uk.gov.justice.hmpps.architecture.model.MoJSignOn
import uk.gov.justice.hmpps.architecture.model.NDH
import uk.gov.justice.hmpps.architecture.model.NID
import uk.gov.justice.hmpps.architecture.model.NOMIS
import uk.gov.justice.hmpps.architecture.model.NationalPrisonRadio
import uk.gov.justice.hmpps.architecture.model.OASys
import uk.gov.justice.hmpps.architecture.model.PolicyTeams
import uk.gov.justice.hmpps.architecture.model.PrepareCaseForSentence
import uk.gov.justice.hmpps.architecture.model.PrisonRegister
import uk.gov.justice.hmpps.architecture.model.PrisonToProbationUpdate
import uk.gov.justice.hmpps.architecture.model.PrisonVisitsBooking
import uk.gov.justice.hmpps.architecture.model.PrisonerContentHub
import uk.gov.justice.hmpps.architecture.model.PrisonerMoney
import uk.gov.justice.hmpps.architecture.model.ProbationAllocationTool
import uk.gov.justice.hmpps.architecture.model.ProbationCaseSampler
import uk.gov.justice.hmpps.architecture.model.ProbationPractitioners
import uk.gov.justice.hmpps.architecture.model.ProbationTeamsService
import uk.gov.justice.hmpps.architecture.model.Reporting
import uk.gov.justice.hmpps.architecture.model.RestrictedPatientsApi
import uk.gov.justice.hmpps.architecture.model.StaffLookupApi
import uk.gov.justice.hmpps.architecture.model.TierService
import uk.gov.justice.hmpps.architecture.model.TokenVerificationApi
import uk.gov.justice.hmpps.architecture.model.UnpaidWorkService
import uk.gov.justice.hmpps.architecture.model.UseOfForce
import uk.gov.justice.hmpps.architecture.model.UserPreferenceApi
import uk.gov.justice.hmpps.architecture.model.WMT
import uk.gov.justice.hmpps.architecture.model.WhereaboutsApi
import uk.gov.justice.hmpps.architecture.views.HMPPSDataAPI
import uk.gov.justice.hmpps.architecture.views.HMPPSDomainAPI
import uk.gov.justice.hmpps.architecture.views.HMPPSInternalAPI

private val MODEL_ITEMS = listOf(
  AdjudicationsApi,
  AnalyticalPlatform,
  AssessRisksAndNeeds,
  UnpaidWorkService,
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
  HMPPSAudit,
  HMPPSAuth,
  HMPPSAPI,
  HMPPSDomainEvents,
  IM,
  Interventions,
  InterventionTeams,
  KeyworkerApi,
  Licences,
  MoJSignOn,
  NationalPrisonRadio,
  Notify,
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
  DigitalPrisonServices,
  PrisonToProbationUpdate,
  PrisonVisitsBooking,
  ProbationAllocationTool,
  ProbationCaseSampler,
  ProbationPractitioners,
  ProbationTeamsService,
  Reporting,
  RestrictedPatientsApi,
  TierService,
  TokenVerificationApi,
  UserPreferenceApi,
  UseOfForce,
  WhereaboutsApi,
  WMT,
  ConsiderARecall,
  PrisonRegister,
  StaffLookupApi,
  CreateAndVaryALicence
)

private val VIEWS = listOf(
  HMPPSInternalAPI,
  HMPPSDomainAPI,
  HMPPSDataAPI
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
  VIEWS.forEach { it.defineViews(views) }

  defineGlobalViews(model, views)
}

fun defineWorkspace(): Workspace {
  val enterprise = Enterprise("HM Prison and Probation Service")
  val workspace = Workspace(enterprise.name, "Systems related to the custody and probation of offenders")
  workspace.id = 56937
  workspace.model.enterprise = enterprise
  workspace.views.configuration.addProperty("plantuml.title", "false")

  defineModelItems(workspace.model)
  changeUndefinedLocationsToInternal(workspace.model)

  defineRelationships()
  defineViews(workspace.model, workspace.views)
  defineStyles(workspace.views.configuration.styles)
  defineDocumentation(workspace)

  return workspace
}
