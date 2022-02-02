package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class HMPPSAPI private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "HMPPS APIs",
        "API Systems"
      )
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {

      views.createContainerView(
        system,
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
        system,
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
        system,
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
