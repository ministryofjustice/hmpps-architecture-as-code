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
  val sentencingPolicy = model.addPerson("Sentencing Policy", "Pseudo-team to capture sentencing policy meeting participants")

  val hmip = model.addPerson("HM Inspectorate of Probation", "Reports to the government on the effectiveness of work with people who offended to reduce reoffending and protect the public")

  val caseNotesToProbation = model.addSoftwareSystem("Case Notes to Probation", "Polls for case notes and pushes them to probation systems")

  caseNotesToProbation.uses(Delius.system, "pushes case notes to")
  EPF.projectManager.interactsWith(sentencingPolicy, "listens to owners of interventions for changes in policy")
  Reporting.ndmis.uses(OffenderManagementInCustody.allocationManager, "sends extracts containing service user allocation to", "email")

  hmip.uses(Reporting.ndmis, "uses data from")
  OffenderManagementInCustody.ldu.uses(Delius.system, "maintains 'shadow' team assignments for service users during prison-to-probation handover in")
}
