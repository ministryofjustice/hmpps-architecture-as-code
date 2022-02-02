package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class HMPPSDomainAPI private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "HMPPS Domain APIs",
        "Domain API Systems"
      )
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {

      views.createContainerView(
        system,
        "HMPPSAPIProbationDomainView",
        "HMPPS Internal Probation Domain API services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("AREA_PROBATION") }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("AREA_PROBATION") }.map { add(it) }
        }
      }

      views.createContainerView(
        system,
        "HMPPSAPIPrisonsDomainView",
        "HMPPS Internal Prisons Domain API services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("AREA_PRISONS") }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("AREA_PRISONS") }.map { add(it) }
        }
      }

      views.createContainerView(
        system,
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
