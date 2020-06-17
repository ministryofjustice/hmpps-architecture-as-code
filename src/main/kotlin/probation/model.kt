package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model

fun probationModel(model: Model) {
  val pContractManager = model.addPerson("Contract Manager for CRCs", null)
  val pEPFManager = model.addPerson("EPF Product Manager", "Product manager for the Effective Proposals Framework tool")
  val pInterventionServices = model.addPerson("Intervention Services Team", "They accredit intervention programmes and do business development of the interventions.")
  val pNDST = model.addPerson("National Delius Support Team", null)
  val pNPSCourtAdmin = model.addPerson("NPS court administrator", null)
  val pNPSOffenderManager = model.addPerson("NPS offender manager", "National Probation Service employed probation officers in custody, court and the community")
  val pNPSProgrammeManager = model.addPerson("NPS programme manager", null)
  val pSentencingPolicy = model.addPerson("Sentencing Policy", "Pseudo-team to capture sentencing policy meeting participants")

  val sCaseNotes = model.addSoftwareSystem("Case Notes to Probation", "Polls for case notes and pushes them to probation systems")
  val sEPF = model.addSoftwareSystem("EPF", "Effective Proposal Framework\nPresents sentencing options to NPS staff in court who are providing sentencing advice to sentencers")
  val sEquip = model.addSoftwareSystem("EQuiP", "Central repository for all step-by-step business processes (in probation?)")
  val sIM = model.addSoftwareSystem("IM", "Interventions Manager\nHolds records of interventions delivered to services users in the community")
  val sDelius = model.addSoftwareSystem("nDelius", "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data")
  val sOASys = model.addSoftwareSystem("OASys", "Offender Assessment System\nAssesses the risks and needs of offenders")
  val sPrepCase = model.addSoftwareSystem("Prepare a Case for Court", "Service for Probation Officers working in magistrates' courts, providing them with a single location to access the defendant information they need to provide sound and timely sentencing guidance to magistrates")
  val sPtoP = model.addSoftwareSystem("Prison to Probation Update", "Listens for events from Prison systems (NOMIS) to update offender sentence information in Probation systems (Delius)").apply {
    setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NOM/pages/1947107651/Prison+to+Probation+Update+-+Delius+DSS+Automatic+updates")
  }
  val sCaseSampler = model.addSoftwareSystem("Probation Case Sampler", "API which produces a representative and evenly distributed list of probation cases within a region and date range which form the basis of an on-site inspection").apply {
    setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NDSS/pages/1989181486/HMIP+Case+Sampling")
  }

  val sNDH = model.addSoftwareSystem("NDH", "NOMIS Data Hub,\nresponsible for pulling/pushing data between HMPPS case management systems")
  val sNomis = model.addSoftwareSystem("NOMIS", "National Offender Management Information System,\nthe case management system for offender data in use in custody - both public and private prisons")

  model.addSoftwareSystem("National Intervention Database (NID) (deprecated)", "Spreadsheet to store intervention details").apply { addTags("deprecated") }

  val pCRCProgrammeManager = model.addPerson("CRC programme manager", "People who provide interventions on behalf of Community Rehabilitation Companies")
  val pCRCOffenderManager = model.addPerson("CRC offender manager", "Probation officers in custody, court and the community employed by intervention providers")
  val pCRCTreatmentManager = model.addPerson("CRC treatment manager", null)
  val sCRCSystem = model.addSoftwareSystem("CRC software systems", null)
  listOf(pCRCProgrammeManager, pCRCOffenderManager, pCRCTreatmentManager, sCRCSystem).forEach { e -> e.addTags("external") }

  pContractManager.uses(pEPFManager, "sends a signed off version of the CRC's Discretionary Services rate card brochure to", null, null)

  pCRCProgrammeManager.uses(pNDST, "opens tickets to update interventions", null, null)

  pCRCOffenderManager.uses(sDelius, "records and reviews assessment decision, sentence plan in", null, null)
  pCRCOffenderManager.uses(sOASys, "records offender risk (attendance, contact, etc.) and assessment in", null, null)
  pCRCOffenderManager.uses(sEquip, "finds information about a process or software in", null, null)

  pCRCTreatmentManager.uses(sIM, "creates new interventions in", null, null)

  pEPFManager.uses(pSentencingPolicy, "listens to owners of interventions for changes in policy", null, null)?.apply {
    setUrl("https://youtu.be/-kbDQ0uK0nM?t=1135")
  }
  pEPFManager.uses(sEPF, "update intervention eligibility for accredited programmes in", null, null)
  pEPFManager.uses(sEPF, "updates interventions table for discretionary services in", null, null)

  pInterventionServices.uses(sIM, "creates new interventions in", null, null)
  pInterventionServices.uses(pEPFManager, "provide programme suitability guide for accredited programme eligibility to", null, null)
  pInterventionServices.uses(sDelius, "creates new interventions in", null, null)

  pNDST.uses(sDelius, "administers everything in", null, null)
  pNDST.uses(sDelius, "updates interventions in", null, null)

  pNPSCourtAdmin.uses(sDelius, "records CAS decision, referrals in", null, null)
  pNPSCourtAdmin.uses(sPrepCase, "captures court judgements in", null, null)

  pNPSOffenderManager.uses(sEquip, "finds rate cards in", null, null)
  pNPSOffenderManager.uses(sPrepCase, "view case defendant details", null, null)
  pNPSOffenderManager.uses(sDelius, "records and reviews assessment decision, sentence plan, pre-sentence report, referrals in", null, null)
  pNPSOffenderManager.uses(sEquip, "finds information about a process or software in", null, null)
  pNPSOffenderManager.uses(sEPF, "enters location, offender needs, assessment score data to receive recommended interventions for licence condition planning from", null, null)
  pNPSOffenderManager.uses(sOASys, "records offender risk (attendance, contact, etc.) and assessment in", null, null)
  pNPSOffenderManager.uses(sEPF, "enters court, location, offender needs, assessment score data to receive a shortlist of recommended interventions for Pre-Sentence Report Proposal from", null, null)

  pNPSProgrammeManager.uses(sIM, "create new interventions in", null, null)

  sCRCSystem.uses(sDelius, "???", null, null)

  sCaseNotes.uses(sDelius, "pushes case notes to", null, null)

  sCaseSampler.uses(sDelius, "gets list of probation cases", null, null)

  sDelius.uses(sIM, "pushes active sentence requirements or licence conditions which are of interest to IM to", "IAPS", null)
  sDelius.uses(sOASys, "offender details, offence details, sentence info are copied into", "NDH", null)

  sIM.uses(sDelius, "pushes contact information of interest to", null, null)

  sNDH.uses(sNomis, "extract offender data", null, null)

  sNomis.uses(sDelius, "offender data is copied into", "NDH", null)
  sNomis.uses(sOASys, "offender data is coped into", "NDH", null)
  sNomis.uses(sPtoP, "notifies changes", null, null)

  sOASys.uses(sDelius, "assessment info, risk measures are copied into", "NDH", null)

  sPrepCase.uses(sDelius, "get offender details from", null, null)
  sPrepCase.uses(sOASys, "get offender assessment details from", null, null)

  sPtoP.uses(sDelius, "update offender sentence information in", null, null)
}
