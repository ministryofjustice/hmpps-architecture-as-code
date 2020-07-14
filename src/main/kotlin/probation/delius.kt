package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

import uk.gov.justice.hmpps.architecture.shared.Tags

class Delius(model: Model) {
  val system: SoftwareSystem

  init {
    system = model.addSoftwareSystem("nDelius",
        "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data")

    val db = system.addContainer("nDelius database", null, "Oracle").apply {
      Tags.DATABASE.addTo(this)
      Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
    }

    val elasticSearchStore = system.addContainer("ElasticSearch store",
        "Data store for Delius content", "ElasticSearch")
        .apply {
          Tags.DATABASE.addTo(this)
          Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        }

    system.addContainer("Community API",
        "API over the nDelius DB used by HMPPS Digital team applications and services", "Java")
        .apply {
          setUrl("https://github.com/ministryofjustice/community-api")
          uses(db, "connects to", "JDBC")
        }

    system.addContainer("OffenderSearch",
        "API over the nDelius offender data held in Elasticsearch", "Java").apply {
      uses(elasticSearchStore, "Queries offender data from nDelius Elasticsearch Index")
    }
  }
}
