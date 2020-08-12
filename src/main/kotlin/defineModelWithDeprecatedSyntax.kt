package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model

fun defineModelWithDeprecatedSyntax(model: Model) {
  // lifted from prison model
  val spo = model.addPerson("Senior Prison Offender Manager", "manages service users and offender managers")
  val pom = model.addPerson("Prison Offender Manager", "responsible for the service users in their prison")
  pom.interactsWith(spo, "managed by")

  NDH(model)
  Pathfinder(model)

  spo.uses(OffenderManagementInCustody.allocationManager, "look at unallocated service users coming from court in")
  pom.uses(OffenderManagementInCustody.allocationManager, "look at service users who need handing over to community in")

  // lifted from probation model
  val contractManager = model.addPerson("Contract Manager for CRCs", null)
  val courtAdmin = model.addPerson("NPS court administrator", null)
  val crcProgrammeManager = model.addPerson("CRC programme manager", "People who provide interventions on behalf of Community Rehabilitation Companies").apply { Tags.PROVIDER.addTo(this) }
  val sentencingPolicy = model.addPerson("Sentencing Policy", "Pseudo-team to capture sentencing policy meeting participants")

  val hmip = model.addPerson("HM Inspectorate of Probation", "Reports to the government on the effectiveness of work with people who offended to reduce reoffending and protect the public")

  val caseNotesToProbation = model.addSoftwareSystem("Case Notes to Probation", "Polls for case notes and pushes them to probation systems")
  val oasys = model.addSoftwareSystem("OASys", "Offender Assessment System\nAssesses the risks and needs of offenders")
  val prepareCaseForCourt = model.addSoftwareSystem("Prepare a Case for Court", "Service for Probation Officers working in magistrates' courts, providing them with a single location to access the defendant information they need to provide sound and timely sentencing guidance to magistrates")
  val prisonToProbation = model.addSoftwareSystem("Prison to Probation Update", "Listens for events from Prison systems (NOMIS) to update offender sentence information in Probation systems (Delius)")
  val probationCaseSampler = model.addSoftwareSystem("Probation Case Sampler", "API which produces a representative and evenly distributed list of probation cases within a region and date range which form the basis of an on-site inspection")
  val wmt = model.addSoftwareSystem("WMT", "Workload Management Tool,\nhelps offender managers schedule their time based on service user risk")

  prisonToProbation.setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NOM/pages/1947107651/Prison+to+Probation+Update+-+Delius+DSS+Automatic+updates")
  probationCaseSampler.setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NDSS/pages/1989181486/HMIP+Case+Sampling")

  caseNotesToProbation.uses(Delius.system, "pushes case notes to")
  contractManager.interactsWith(EPF.projectManager, "sends a signed off version of the CRC's Discretionary Services rate card brochure to")
  courtAdmin.uses(Delius.system, "records CAS decision, referrals in")
  courtAdmin.uses(prepareCaseForCourt, "captures court judgements in")
  ProbationPractitioners.crc.uses(oasys, "records offender risk (attendance, contact, etc.) and assessment in")
  crcProgrammeManager.interactsWith(Delius.supportTeam, "opens tickets to update interventions")
  Delius.system.uses(oasys, "offender details, offence details, sentence info are copied into", "NDH")
  EPF.projectManager.interactsWith(sentencingPolicy, "listens to owners of interventions for changes in policy")
  Reporting.ndmis.uses(OffenderManagementInCustody.allocationManager, "sends extracts containing service user allocation to", "email")
  NOMIS.system.uses(Delius.system, "offender data is copied into", "NDH")
  NOMIS.system.uses(oasys, "offender data is coped into", "NDH")
  NOMIS.system.uses(prisonToProbation, "notifies changes")
  ProbationPractitioners.nps.uses(oasys, "records offender risk (attendance, contact, etc.) and assessment in")
  ProbationPractitioners.nps.uses(prepareCaseForCourt, "view case defendant details")
  ProbationPractitioners.nps.uses(wmt, "finds out their community case load by looking at")
  oasys.uses(Delius.system, "assessment info, risk measures are copied into", "NDH")
  prepareCaseForCourt.uses(Delius.system, "get offender details from")
  prepareCaseForCourt.uses(oasys, "get offender assessment details from")
  prisonToProbation.uses(Delius.system, "update offender sentence information in")
  probationCaseSampler.uses(Delius.system, "gets list of probation cases")
  wmt.uses(Reporting.ndmis, "draws offender risk and allocation data from")

  hmip.uses(Reporting.ndmis, "uses data from")
  OffenderManagementInCustody.ldu.uses(Delius.system, "maintains 'shadow' team assignments for service users during prison-to-probation handover in")
}
