package uk.gov.justice.hmpps.architecture.views

import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.model.Delius
import uk.gov.justice.hmpps.architecture.model.HMPPSAPI
import uk.gov.justice.hmpps.architecture.model.NOMIS
import uk.gov.justice.hmpps.architecture.model.OASys

class HMPPSInternalAPI private constructor() {

  companion object : HMPPSView {

    override fun defineViews(views: ViewSet) {

      views.createContainerView(
        HMPPSAPI.system,
        "HMPPSAPIView",
        "HMPPS API services"
      ).apply {
        model.softwareSystems.filter {
          it.hasTag("DOMAIN_API") || it.hasTag("DATA_API")
        }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter {
            it.hasTag("DOMAIN_API") || it.hasTag("DATA_API")
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
        HMPPSAPI.system,
        "HMPPSAPIProbationAreaView",
        "HMPPS API Probation Area Services "
      ).apply {
        model.softwareSystems.filter {
          (it.hasTag("DOMAIN_API") || it.hasTag("DATA_API")) && it.hasTag("AREA_PROBATION")
        }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter {
            (it.hasTag("DOMAIN_API") || it.hasTag("DATA_API")) && it.hasTag("AREA_PROBATION")
          }.map { add(it) }
        }

        // API data sources
        add(Delius.database)
        add(Delius.offenderElasticsearchStore)
        add(OASys.oasysDB)
      }

      views.createContainerView(
        HMPPSAPI.system,
        "HMPPSAPIPrisonsAreaView",
        "HMPPS API Prisons Area Services "
      ).apply {
        model.softwareSystems.filter {
          (it.hasTag("DOMAIN_API") || it.hasTag("DATA_API")) && it.hasTag("AREA_PRISONS")
        }.map { add(it) }

        model.softwareSystems.forEach {
          it.containers.filter {
            (it.hasTag("DOMAIN_API") || it.hasTag("DATA_API")) && it.hasTag("AREA_PRISONS")
          }.map { add(it) }
        }

        // API data sources
        add(NOMIS.db)
        add(NOMIS.elasticSearchStore)
      }
    }
  }
}
