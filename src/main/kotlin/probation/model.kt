package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model
import uk.gov.justice.hmpps.architecture.prison.NOMIS
import uk.gov.justice.hmpps.architecture.prison.OffenderManagementInCustody
import uk.gov.justice.hmpps.architecture.shared.Tags

fun probationModel(model: Model) {
  model.setImpliedRelationshipsStrategy(
    CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy()
  )

  val contractManager = model.addPerson("Contract Manager for CRCs", null)
  val courtAdmin = model.addPerson("NPS court administrator", null)
  val crcOffenderManager = model.addPerson("CRC offender manager", "Probation officers in custody, court and the community employed by intervention providers").apply { Tags.PROVIDER.addTo(this) }
  val crcProgrammeManager = model.addPerson("CRC programme manager", "People who provide interventions on behalf of Community Rehabilitation Companies").apply { Tags.PROVIDER.addTo(this) }
  val crcTreatmentManager = model.addPerson("CRC treatment manager", null).apply { Tags.PROVIDER.addTo(this) }
  val deliusSupport = model.addPerson("National Delius Support Team", null)
  val interventionServices = model.addPerson("Intervention Services Team", "They accredit intervention programmes and do business development of the interventions.")
  val npsOffenderManager = model.addPerson("NPS offender manager", "National Probation Service employed probation officers in custody, court and the community")
  val npsProgrammeManager = model.addPerson("NPS programme manager", null)
  val sentencingPolicy = model.addPerson("Sentencing Policy", "Pseudo-team to capture sentencing policy meeting participants")

  val communityPerformance = model.addPerson("Community Performance team", "Reporting on HMPPS performance in the community")
  val crcPerformanceAnalyst = model.addPerson("CRC performance analyst", null).apply { Tags.PROVIDER.addTo(this) }
  val dataInnovation = model.addPerson("Data Innovation, Analysis and Linking team", "Works on linked data from various non-HMPPS government departments")
  val hmip = model.addPerson("HM Inspectorate of Probation", "Reports to the government on the effectiveness of work with people who offended to reduce reoffending and protect the public")
  val nart = model.addPerson("National Applications Reporting Team", "Responsible for the delivery of reporting to stakeholders")
  val npsPerformanceOfficer = model.addPerson("NPS performance and quality officer", null)
  val prisonPerformance = model.addPerson("Prison Performance team", "Reporting on HMPPS performance in prison")

  listOf(communityPerformance, prisonPerformance, dataInnovation).forEach { it.addProperty("org", "DASD") }

  val caseNotesToProbation = model.addSoftwareSystem("Case Notes to Probation", "Polls for case notes and pushes them to probation systems")
  val crcSystem = model.addSoftwareSystem("CRC software systems", null).apply { Tags.PROVIDER.addTo(this) }
  val equip = model.addSoftwareSystem("EQuiP", "Central repository for all step-by-step business processes (in probation?)")
  val ndmis = model.addSoftwareSystem("NDMIS", "National Delius Management Information System,\nresponsible for providing reporting on nDelius data")
  val nid = model.addSoftwareSystem("National Intervention Database (NID) (deprecated)", "Spreadsheet to store intervention details").apply { Tags.DEPRECATED.addTo(this) }
  val oasys = model.addSoftwareSystem("OASys", "Offender Assessment System\nAssesses the risks and needs of offenders")
  val prepareCaseForCourt = model.addSoftwareSystem("Prepare a Case for Court", "Service for Probation Officers working in magistrates' courts, providing them with a single location to access the defendant information they need to provide sound and timely sentencing guidance to magistrates")
  val prisonToProbation = model.addSoftwareSystem("Prison to Probation Update", "Listens for events from Prison systems (NOMIS) to update offender sentence information in Probation systems (Delius)")
  val probationCaseSampler = model.addSoftwareSystem("Probation Case Sampler", "API which produces a representative and evenly distributed list of probation cases within a region and date range which form the basis of an on-site inspection")
  val wmt = model.addSoftwareSystem("WMT", "Workload Management Tool,\nhelps offender managers schedule their time based on service user risk")

  NOMIS.defineModelEntities(model)
  OffenderManagementInCustody.defineModelEntities(model)
  NOMIS.system.apply { Tags.PRISON_SERVICE.addTo(this) }
  OffenderManagementInCustody.system.apply { Tags.PRISON_SERVICE.addTo(this) }

  prisonToProbation.setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NOM/pages/1947107651/Prison+to+Probation+Update+-+Delius+DSS+Automatic+updates")
  probationCaseSampler.setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NDSS/pages/1989181486/HMIP+Case+Sampling")

  caseNotesToProbation.uses(Delius.system, "pushes case notes to")
  contractManager.interactsWith(EPF.projectManager, "sends a signed off version of the CRC's Discretionary Services rate card brochure to")
  courtAdmin.uses(Delius.system, "records CAS decision, referrals in")
  courtAdmin.uses(prepareCaseForCourt, "captures court judgements in")
  crcOffenderManager.uses(Delius.system, "records and reviews assessment decision, sentence plan in")
  crcOffenderManager.uses(equip, "finds information about a process or software in")
  crcOffenderManager.uses(oasys, "records offender risk (attendance, contact, etc.) and assessment in")
  crcProgrammeManager.interactsWith(deliusSupport, "opens tickets to update interventions")
  crcSystem.uses(Delius.system, "???")
  crcTreatmentManager.uses(IM.system, "creates new interventions in")
  Delius.system.uses(oasys, "offender details, offence details, sentence info are copied into", "NDH")
  deliusSupport.uses(Delius.system, "administers everything in")
  deliusSupport.uses(Delius.system, "updates interventions in")
  EPF.projectManager.interactsWith(sentencingPolicy, "listens to owners of interventions for changes in policy")
  interventionServices.interactsWith(EPF.projectManager, "provide programme suitability guide for accredited programme eligibility to")
  interventionServices.uses(Delius.system, "creates new interventions in")
  interventionServices.uses(IM.system, "creates new interventions in")
  ndmis.uses(Delius.system, "extracts and transforms data from")
  ndmis.uses(OffenderManagementInCustody.allocationManager, "sends extracts containing service user allocation to", "email")
  NOMIS.system.uses(Delius.system, "offender data is copied into", "NDH")
  NOMIS.system.uses(oasys, "offender data is coped into", "NDH")
  NOMIS.system.uses(prisonToProbation, "notifies changes")
  npsOffenderManager.uses(Delius.system, "records and reviews assessment decision, sentence plan, pre-sentence report, referrals in")
  npsOffenderManager.uses(EPF.system, "enters court, location, offender needs, assessment score data to receive a shortlist of recommended interventions for Pre-Sentence Report Proposal from")
  npsOffenderManager.uses(EPF.system, "enters location, offender needs, assessment score data to receive recommended interventions for licence condition planning from")
  npsOffenderManager.uses(equip, "finds information about a process or software in")
  npsOffenderManager.uses(equip, "finds rate cards in")
  npsOffenderManager.uses(oasys, "records offender risk (attendance, contact, etc.) and assessment in")
  npsOffenderManager.uses(prepareCaseForCourt, "view case defendant details")
  npsOffenderManager.uses(wmt, "finds out their community case load by looking at")
  npsProgrammeManager.uses(IM.system, "create new interventions in")
  oasys.uses(Delius.system, "assessment info, risk measures are copied into", "NDH")
  prepareCaseForCourt.uses(Delius.system, "get offender details from")
  prepareCaseForCourt.uses(oasys, "get offender assessment details from")
  prisonToProbation.uses(Delius.system, "update offender sentence information in")
  probationCaseSampler.uses(Delius.system, "gets list of probation cases")
  wmt.uses(ndmis, "draws offender risk and allocation data from")

  communityPerformance.uses(ndmis, "uses reports in")
  crcPerformanceAnalyst.uses(ndmis, "uses reports in")
  dataInnovation.uses(ndmis, "uses data from")
  hmip.uses(ndmis, "uses data from")
  nart.uses(ndmis, "creates reports in")
  npsPerformanceOfficer.uses(ndmis, "uses reports in")
  OffenderManagementInCustody.ldu.uses(Delius.system, "maintains 'shadow' team assignments for service users during prison-to-probation handover in")
  prisonPerformance.uses(ndmis, "to provide details of offenders released into the community, looks into")
}
