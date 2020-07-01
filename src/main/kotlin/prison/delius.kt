package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem

class nDelius(model: Model) {
  val system: SoftwareSystem
  val db: Container

  init {
    val delius = model.addSoftwareSystem("nDelius",
        "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data")

    db = delius.addContainer("nDelius database", null, "Oracle").apply {
      addTags("database")
    }

    delius.addContainer("CommunityAPI",
        "API over the nDelius DB used by HMPPS Digital team applications and services", "Java")
        .apply {
          setUrl("https://github.com/ministryofjustice/community-api")
          uses(db, "JDBC")
        }

    val elasticSearch = delius.addContainer("ElasticSearch", "Elasticsearch index of nDelius data",
        null).apply {
      addTags("External")
    }

    delius.addContainer("OffenderSearch",
        "API over the nDelius offender data held in Elasticsearch", "Java").apply {
      uses(elasticSearch, "Queries offender data from nDelius Elasticsearch Index")
    }

    system = delius
  }
}
