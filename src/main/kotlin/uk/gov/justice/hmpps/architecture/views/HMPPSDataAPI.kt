package uk.gov.justice.hmpps.architecture.views

import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.model.AssessRisksAndNeeds
import uk.gov.justice.hmpps.architecture.model.Delius
import uk.gov.justice.hmpps.architecture.model.HMPPSAPI
import uk.gov.justice.hmpps.architecture.model.NOMIS
import uk.gov.justice.hmpps.architecture.model.OASys

class HMPPSDataAPI private constructor() {

  companion object : HMPPSView {

    override fun defineViews(views: ViewSet) {
      views.createContainerView(
        HMPPSAPI.system,
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
        HMPPSAPI.system,
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
        HMPPSAPI.system,
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
        HMPPSAPI.system,
        "HMPPSAPIOASysDataView",
        "HMPPS OASys Data API services"
      ).apply {

        // API services
        add(OASys.assessmentsApi)
        add(OASys.ORDSApi)
        add(AssessRisksAndNeeds.riskNeedsService)

        // API data sources
        add(OASys.oasysDB)
      }
    }
  }
}
