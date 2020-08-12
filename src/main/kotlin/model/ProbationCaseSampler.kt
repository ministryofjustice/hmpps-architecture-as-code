package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class ProbationCaseSampler private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Probation Case Sampler",
        """
        API which produces a representative and evenly distributed list of probation cases
        within a region and date range which form the basis of an on-site inspection
        """.trimIndent()
      ).apply {
        setUrl("https://dsdmoj.atlassian.net/wiki/spaces/NDSS/pages/1989181486/HMIP+Case+Sampling")
      }
    }

    override fun defineRelationships() {
      system.uses(Delius.system, "gets list of probation cases from")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
