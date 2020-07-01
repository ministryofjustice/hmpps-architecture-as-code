package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model

import uk.gov.justice.hmpps.architecture.shared.Tags

fun probationModel(model: Model) {
  val contractManager = model.addPerson("Contract Manager for CRCs", null)
  val courtAdmin = model.addPerson("NPS court administrator", null)
  val crcOffenderManager = model.addPerson("CRC offender manager", "Probation officers in custody, court and the community employed by intervention providers").apply { addTags("external") }
  val crcProgrammeManager = model.addPerson("CRC programme manager", "People who provide interventions on behalf of Community Rehabilitation Companies").apply { addTags("external") }
  val crcTreatmentManager = model.addPerson("CRC treatment manager", null).apply { addTags(Tags.EXTERNAL.toString()) }
  val deliusSupport = model.addPerson("National Delius Support Team", null)
  val epfManager = model.addPerson("EPF Product Manager", "Product manager for the Effective Proposals Framework tool")
  val interventionServices = model.addPerson("Intervention Services Team", "They accredit intervention programmes and do business development of the interventions.")
  val npsOffenderManager = model.addPerson("NPS offender manager", "National Probation Service employed probation officers in custody, court and the community")
  val npsProgrammeManager = model.addPerson("NPS programme manager", null)
  val sentencingPolicy = model.addPerson("Sentencing Policy", "Pseudo-team to capture sentencing policy meeting participants")

  val caseNotesToProbation = model.addSoftwareSystem("Case Notes to Probation", "Polls for case notes and pushes them to probation systems")
  val crcSystem = model.addSoftwareSystem("CRC software systems", null).apply { addTags(Tags.EXTERNAL.toString()) }
  val delius = model.addSoftwareSystem("nDelius", "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data")
  val epf = model.addSoftwareSystem("EPF", "Effective Proposal Framework\nPresents sentencing options to NPS staff in court who are providing sentencing advice to sentencers")
  val equip = model.addSoftwareSystem("EQuiP", "Central repository for all step-by-step business processes (in probation?)")
  val interventionsManager = model.addSoftwareSystem("IM", "Interventions Manager\nHolds records of interventions delivered to services users in the community")
  val ndh = model.addSoftwareSystem("NDH", "NOMIS Data Hub,\nresponsible for pulling/pushing data between HMPPS case management systems")
  val nid = model.addSoftwareSystem("National Intervention Database (NID) (deprecated)", "Spreadsheet to store intervention details").apply { addTags(Tags.DEPRECATED.toString()) }
  val nomis = model.addSoftwareSystem("NOMIS", "National Offender Management Information System,\nthe case management system for offender data in use in custody - both public and private prisons")
  val oasys = model.addSoftwareSystem("OASys", "Offender Assessment System\nAssesses the risks and needs of offenders")
  val prepareCaseForCourt = model.addSoftwareSystem("Prepare a Case for Court", "Service for Probation Officers working in magistrates' courts, providing them with a single location to access the defendant information they need to provide sound and timely sentencing guidance to magistrates")
  val prisonToProbation = model.addSoftwareSystem("Prison to Probation Update", "Listens for events from Prison systems (NOMIS) to update offender sentence information in Probation systems (Delius)")
  val probationCaseSampler = model.addSoftwareSystem("Probation Case Sampler", "API which produces a representative and evenly distributed list of probation cases within a region and date range which form the basis of an on-site inspection")

  prisonToProbation.setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NOM/pages/1947107651/Prison+to+Probation+Update+-+Delius+DSS+Automatic+updates")
  probationCaseSampler.setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NDSS/pages/1989181486/HMIP+Case+Sampling")

  caseNotesToProbation.uses(delius, "pushes case notes to")
  contractManager.interactsWith(epfManager, "sends a signed off version of the CRC's Discretionary Services rate card brochure to")
  courtAdmin.uses(delius, "records CAS decision, referrals in")
  courtAdmin.uses(prepareCaseForCourt, "captures court judgements in")
  crcOffenderManager.uses(delius, "records and reviews assessment decision, sentence plan in")
  crcOffenderManager.uses(equip, "finds information about a process or software in")
  crcOffenderManager.uses(oasys, "records offender risk (attendance, contact, etc.) and assessment in")
  crcProgrammeManager.interactsWith(deliusSupport, "opens tickets to update interventions")
  crcSystem.uses(delius, "???")
  crcTreatmentManager.uses(interventionsManager, "creates new interventions in")
  delius.uses(interventionsManager, "pushes active sentence requirements or licence conditions which are of interest to IM to", "IAPS")
  delius.uses(oasys, "offender details, offence details, sentence info are copied into", "NDH")
  deliusSupport.uses(delius, "administers everything in")
  deliusSupport.uses(delius, "updates interventions in")
  epfManager.interactsWith(sentencingPolicy, "listens to owners of interventions for changes in policy")
  epfManager.uses(epf, "update intervention eligibility for accredited programmes in")
  epfManager.uses(epf, "updates interventions table for discretionary services in")
  interventionServices.interactsWith(epfManager, "provide programme suitability guide for accredited programme eligibility to")
  interventionServices.uses(delius, "creates new interventions in")
  interventionServices.uses(interventionsManager, "creates new interventions in")
  interventionsManager.uses(delius, "pushes contact information of interest to")
  ndh.uses(nomis, "extract offender data")
  nomis.uses(delius, "offender data is copied into", "NDH")
  nomis.uses(oasys, "offender data is coped into", "NDH")
  nomis.uses(prisonToProbation, "notifies changes")
  npsOffenderManager.uses(delius, "records and reviews assessment decision, sentence plan, pre-sentence report, referrals in")
  npsOffenderManager.uses(epf, "enters court, location, offender needs, assessment score data to receive a shortlist of recommended interventions for Pre-Sentence Report Proposal from")
  npsOffenderManager.uses(epf, "enters location, offender needs, assessment score data to receive recommended interventions for licence condition planning from")
  npsOffenderManager.uses(equip, "finds information about a process or software in")
  npsOffenderManager.uses(equip, "finds rate cards in")
  npsOffenderManager.uses(oasys, "records offender risk (attendance, contact, etc.) and assessment in")
  npsOffenderManager.uses(prepareCaseForCourt, "view case defendant details")
  npsProgrammeManager.uses(interventionsManager, "create new interventions in")
  oasys.uses(delius, "assessment info, risk measures are copied into", "NDH")
  prepareCaseForCourt.uses(delius, "get offender details from")
  prepareCaseForCourt.uses(oasys, "get offender assessment details from")
  prisonToProbation.uses(delius, "update offender sentence information in")
  probationCaseSampler.uses(delius, "gets list of probation cases")
}
