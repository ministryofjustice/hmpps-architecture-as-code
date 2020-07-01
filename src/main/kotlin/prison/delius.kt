package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

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

    db = system.addContainer("nDelius database", null, "Oracle").apply {
      addTags(DATABASE_TAG)
      addTags(SOFTWARE_AS_A_SERVICE_TAG)
    }

    val elasticSearchStore = system.addContainer("ElasticSearch store",
        "Data store for feedback collection, and indexing for Drupal CMS content", "ElasticSearch")
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
