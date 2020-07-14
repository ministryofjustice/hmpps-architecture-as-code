package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.shared.CloudPlatform
import uk.gov.justice.hmpps.architecture.shared.Tags

class NOMIS private constructor() {

  companion object: HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var db: Container
    lateinit var elite2api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("NOMIS", """
      National Offender Management Information System,
      the case management system for offender data in use in custody - both public and private prisons
      """.trimIndent()).apply {
        setLocation(Location.Internal)
      }

      db = system.addContainer("NOMIS database", null, "Oracle").apply {
        Tags.DATABASE.addTo(this)
      }

      system.addContainer("NOMIS Web Application",
          "Web Application fronting the NOMIS DB - used by Digital Prison team applications and services",
          "Weblogic App Sever").apply {
        uses(db, "connects to", "JDBC")
      }

      elite2api = system.addContainer("Elite2 API",
          "API over the NOMIS DB used by Digital Prison team applications and services", "Java")
          .apply {
            setUrl("https://github.com/ministryofjustice/elite2-api")
            uses(db, "connects to", "JDBC")
          }

      val elasticSearchStore = system.addContainer("ElasticSearch store",
          "Data store for NOMIS content", "ElasticSearch")
          .apply {
            Tags.DATABASE.addTo(this)
            Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
            CloudPlatform.elasticsearch.add(this)
          }

      system.addContainer("PrisonerSearch", "API over the NOMIS prisoner data held in Elasticsearch",
          "Java").apply {
        uses(elasticSearchStore, "Queries prisoner data from NOMIS Elasticsearch Index")
        CloudPlatform.kubernetes.add(this)
      }

      system.addContainer("Custody API (Deprecated)",
          "(Deprecated) Offender API.  The service provides REST access to the Nomis Oracle DB offender information. Deprecated - please use Elite2 API instead.",
          null).apply {
        setUrl("https://github.com/ministryofjustice/custody-api")
        Tags.DEPRECATED.addTo(this)
        uses(db, "connects to", "JDBC")
      }

      system.addContainer("NOMIS API (Deprecated)",
          "(Deprecated) REST API for NOMIS which connects to Oracle DB. Deprecated - please use " + elite2api.getName() + " instead.",
          null).apply {
        setUrl("https://github.com/ministryofjustice/nomis-api")
        Tags.DEPRECATED.addTo(this)
        uses(db, "connects to", "JDBC")
      }

      system.addContainer("Offender Events",
          "Publishes Events about offender change to Pub / Sub Topics.", "Java").apply {
        setUrl("https://github.com/ministryofjustice/offender-events")
        uses(db, "connects to", "JDBC")
        CloudPlatform.sqs.add(this)
        CloudPlatform.sns.add(this)
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createContainerView(system, "nomiscontainer", null).apply {
        addDefaultElements()
        enableAutomaticLayout()
      }
    }
  }
}
