package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

import uk.gov.justice.hmpps.architecture.shared.Tags

class DELIUS(model: Model) {
  val system: SoftwareSystem
  val db: Container

  companion object {
    const val DATABASE_TAG = "database";
    const val SOFTWARE_AS_A_SERVICE_TAG = "SAAS";
  }

  /**
   *
   */

  init {

    system = model.addSoftwareSystem("nDelius",
        "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data")

    db = delius.addContainer("nDelius database", null, "Oracle").apply {
      Tags.DATABASE.addTo(this)
      Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
    }

    val elasticSearchStore = system.addContainer("ElasticSearch store",
        "Data store for Delius content", "ElasticSearch")
        .apply {
          addTags(DATABASE_TAG)
          addTags(SOFTWARE_AS_A_SERVICE_TAG)
        }

    system.addContainer("CommunityAPI",
        "API over the nDelius DB used by HMPPS Digital team applications and services", "Java")
        .apply {
          setUrl("https://github.com/ministryofjustice/community-api")
          uses(db, "JDBC")
        }

    system.addContainer("OffenderSearch",

        "API over the nDelius offender data held in Elasticsearch", "Java").apply {
      uses(elasticSearchStore, "Queries offender data from nDelius Elasticsearch Index")
    }

  }
}
