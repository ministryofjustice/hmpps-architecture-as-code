package uk.gov.justice.hmpps.architecture.views

import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.model.Delius
import uk.gov.justice.hmpps.architecture.model.HMPPSAPI
import uk.gov.justice.hmpps.architecture.model.HMPPSAuth
import uk.gov.justice.hmpps.architecture.model.NOMIS

class HMPPSDomainAPI private constructor() {
  companion object : HMPPSView {
    override fun defineViews(views: ViewSet) {

      views.createContainerView(
        HMPPSAPI.system,
        "HMPPSAPIProbationDomainView",
        "HMPPS Internal Probation Domain API services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("AREA_PROBATION") }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("AREA_PROBATION") }.map { add(it) }
        }
      }

      views.createContainerView(
        HMPPSAPI.system,
        "HMPPSAPIPrisonsDomainView",
        "HMPPS Internal Prisons Domain API services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("AREA_PRISONS") }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("AREA_PRISONS") }.map { add(it) }
        }
      }

      views.createContainerView(
        HMPPSAPI.system,
        "HMPPSAPIAuthView",
        "HMPPS Internal API Authentication services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("AUTH_API") }.map { add(it) }
        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("AUTH_API") }.map { add(it) }
        }

        add(Delius.communityApi)
        add(Delius.database)
        add(NOMIS.prisonApi)
        add(NOMIS.db)
        add(HMPPSAuth.database)
      }
    }
  }
}
