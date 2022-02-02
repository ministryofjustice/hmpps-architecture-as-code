package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class NOMIS private constructor() {

  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var db: Container
    lateinit var offenderSearch: Container
    lateinit var custodyApi: Container
    lateinit var prisonApi: Container
    lateinit var elasticSearchStore: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "NOMIS",
        "(National Offender Management Information System) The case management system for offender data in use in custody - both public and private prisons"
      )

      db = system.addContainer("NOMIS database", null, "Oracle").apply {
        Tags.DATABASE.addTo(this)
      }

      custodyApi = system.addContainer(
        "Custody API (Deprecated)",
        "Provided REST access to the Nomis Oracle DB offender information - deprecated; use prison-api instead",
        "Java"
      ).apply {
        Tags.DATA_API.addTo(this)
        Tags.AREA_PRISONS.addTo(this)
        Tags.DEPRECATED.addTo(this)
        setUrl("https://github.com/ministryofjustice/custody-api")
        uses(db, "connects to", "JDBC")
      }

      prisonApi = system.addContainer(
        "Prison API",
        "API over the NOMIS DB used by Digital Prison team applications and services", "Java"
      ).apply {
        Tags.DATA_API.addTo(this)
        Tags.AREA_PRISONS.addTo(this)
        addProperty("previous-name", "Elite2 API")
        url = "https://github.com/ministryofjustice/prison-api"
        uses(db, "connects to", "JDBC")
      }

      system.addContainer(
        "DPS Web Application",
        "DPS - used by Digital Prison team applications and services",
        "HTTPS"
      ).apply {
        uses(prisonApi, "connects to", "RestHTML")
        uses(Curious.system, "Consumes")
      }

      elasticSearchStore = system.addContainer(
        "ElasticSearch store",
        "Data store for NOMIS content", "ElasticSearch"
      ).apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.elasticsearch.add(this)
      }

      offenderSearch = system.addContainer(
        "PrisonerSearch", "API over the NOMIS prisoner data held in Elasticsearch",
        "Kotlin"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PRISONS.addTo(this)
        uses(elasticSearchStore, "Queries prisoner data from NOMIS Elasticsearch Index")
        setUrl("https://github.com/ministryofjustice/prisoner-offender-search")
        CloudPlatform.kubernetes.add(this)
      }

      system.addContainer(
        "Prison Offender Events",
        "Publishes Events about prisoner changes to Pub / Sub Topics.", "Kotlin"
      ).apply {
        setUrl("https://github.com/ministryofjustice/prison-offender-events")
        uses(db, "connects to", "JDBC")
        CloudPlatform.sqs.add(this)
        CloudPlatform.sns.add(this)
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      system.uses(PrisonToProbationUpdate.system, "notifies changes")
    }

    override fun defineViews(views: ViewSet) {
      views.createContainerView(system, "nomiscontainer", null).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }
    }
  }
}
