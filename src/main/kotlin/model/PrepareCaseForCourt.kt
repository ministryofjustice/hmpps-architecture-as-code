package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class PrepareCaseForCourt private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Prepare a Case for Court",
        """
        Service for Probation Officers working in magistrates' courts, providing them with a
        single location to access the defendant information they need to provide sound and
        timely sentencing guidance to magistrates
        """.trimIndent()
      )
    }

    override fun defineRelationships() {
      CourtUsers.courtAdministrator.uses(system, "captures court judgements in")
      ProbationPractitioners.nps.uses(system, "view case defendant details")

      system.uses(Delius.system, "get offender details from")
      system.uses(OASys.system, "get offender assessment details from")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
