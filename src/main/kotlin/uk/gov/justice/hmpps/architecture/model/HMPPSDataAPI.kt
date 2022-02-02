package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class HMPPSDataAPI private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "HMPPS Data APIs",
        "Internal Data API Systems"
      )
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {

      views.createContainerView(
        system,
        "HMPPSAPIProbationDataView",
        "HMPPS Probation Data API services"
      ).apply {
        model.softwareSystems.filter {
          it.hasTag("AREA_PROBATION") && it.hasTag("DATA_API")
        }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter {
            it.hasTag("AREA_PROBATION") && it.hasTag("DATA_API")
          }.map { add(it) }
        }

        // API data sources
        add(Delius.database)
        add(Delius.offenderElasticsearchStore)
        add(OASys.oasysDB)
      }

      views.createContainerView(
        system,
        "HMPPSAPIPrisonsDataView",
        "HMPPS Prisons Data API services"
      ).apply {
        model.softwareSystems.filter {
          it.hasTag("AREA_PRISONS") && it.hasTag("DATA_API")
        }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter {
            it.hasTag("AREA_PRISONS") && it.hasTag("DATA_API")
          }.map { add(it) }
        }

        // API data sources
        add(NOMIS.db)
        add(NOMIS.offenderSearch)
        add(NOMIS.elasticSearchStore)
      }

      views.createContainerView(
        system,
        "HMPPSAPIDeliusDataView",
        "HMPPS Delius Data API services"
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
        "HMPPSAPIOASysDataView",
        "HMPPS OASys Data API services"
      ).apply {

        // API services
        add(OASys.assessmentsApi)
        add(OASys.assessmentsUpdateApi)

        // API data sources
        add(OASys.oasysDB)
      }
    }
  }
}
