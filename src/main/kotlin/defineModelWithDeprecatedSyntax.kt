package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model

fun defineModelWithDeprecatedSyntax(model: Model) {

  Pathfinder(model)

  // lifted from probation model
  val hmip = model.addPerson("HM Inspectorate of Probation", "Reports to the government on the effectiveness of work with people who offended to reduce reoffending and protect the public")

  hmip.uses(Reporting.ndmis, "uses data from")
}
