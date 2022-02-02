package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class HMPPSAPIInternal private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "HMPPS Internal APIs",
        "Internal API Systems"
      )
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {

      views.createContainerView(
        system,
        "HMPPSInternalAPIView",
        "HMPPS Internal API services"
      ).apply {
        model.softwareSystems.filter {
          it.hasTag("PROBATION_API") || it.hasTag("PRISONS_API")
        }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter {
            it.hasTag("PROBATION_API") || it.hasTag("PRISONS_API")
          }.map { add(it) }
        }

        // API data sources
        add(NOMIS.db)
        add(NOMIS.elasticSearchStore)
        add(Delius.database)
        add(Delius.offenderElasticsearchStore)
        add(OASys.oasysDB)
      }

      views.createContainerView(
        system,
        "HMPPSInternalAPIProbationView",
        "HMPPS Internal Probation API services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("PROBATION_API") }.map { add(it) }
        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("PROBATION_API") }.map { add(it) }
        }

        // API data sources
        add(Delius.database)
        add(Delius.offenderElasticsearchStore)
        add(OASys.oasysDB)
        add(model.getSoftwareSystemWithName("Pathfinder")!!.getContainerWithName("Pathfinder Database"))
      }

      views.createContainerView(
        system,
        "HMPPSInternalAPIPrisonsView",
        "HMPPS Internal Prisons API services"
      ).apply {
        model.softwareSystems.filter { it.hasTag("PRISONS_API") }.map { add(it) }
        model.softwareSystems.forEach {
          it.containers.filter { it.hasTag("PRISONS_API") }.map { add(it) }
        }

        // API data sources
        add(NOMIS.db)
        add(model.getSoftwareSystemWithName("Pathfinder")!!.getContainerWithName("Pathfinder Database"))
        add(NOMIS.offenderSearch)
        add(NOMIS.elasticSearchStore)
      }

      views.createContainerView(
        system,
        "HMPPSInternalAPIDeliusView",
        "HMPPS Internal Delius API services"
      ).apply {

        // API services
        add(Delius.communityApi)
        add(Delius.deliusApi)
        add(Delius.offenderSearch)

        // API data sources
        add(Delius.database)
        add(Delius.offenderElasticsearchStore)
      }

      views.createContainerView(
        system,
        "HMPPSInternalAPIOASysView",
        "HMPPS Internal OASys API services"
      ).apply {

        // API services
        add(OASys.assessmentsApi)
        add(OASys.assessmentsUpdateApi)

        // API data sources
        add(OASys.oasysDB)
      }

      views.createContainerView(
        system,
        "HMPPSInternalAPIAuthView",
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
