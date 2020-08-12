package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class OASys private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("Offender Assessment System", "(OASys) Assesses the risks and needs of offenders")
    }

    override fun defineRelationships() {
      Delius.system.uses(system, "offender details, offence details, sentence info are copied into", "NDH")
      ProbationPractitioners.nps.uses(system, "records offender risk (attendance, contact, etc.) and assessment in")
      ProbationPractitioners.crc.uses(system, "records offender risk (attendance, contact, etc.) and assessment in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
