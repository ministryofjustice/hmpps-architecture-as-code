package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class PrisonerContentHub(model: Model) {
  val system: SoftwareSystem
  val contentHubFrontend: Container

  companion object { 
    const val DATABASE_TAG = "database";
    const val CLOUD_PLATFORM_TAG = "cloud_platform";
    const val SOFTWARE_AS_A_SERVICE_TAG = "SAAS";
  } 
  
  /**
   * TODO: add Prisoner internet infrastructure, AD
   * TODO: add BT PINS
   */

  init {

    system = model.addSoftwareSystem(
      "Prisoner Content Hub", 
      """
      The Prisoner Content Hub is a platform for prisoners to access data, content and services supporting individual progression and freeing up staff time.
      """.trimIndent())

    val elasticSearchStore = system.addContainer("ElasticSearch store", "Data store for feedback collection, and indexing for Drupal CMS content", "ElasticSearch").apply {
      addTags(DATABASE_TAG)
      addTags(CLOUD_PLATFORM_TAG)
      addTags(SOFTWARE_AS_A_SERVICE_TAG)
    }

    val drupalDatabase = system.addContainer("Drupal database", null, "MariaDB").apply {
      addTags(DATABASE_TAG)
      addTags(CLOUD_PLATFORM_TAG)
      addTags(SOFTWARE_AS_A_SERVICE_TAG)
    }

    val s3ContentStore = system.addContainer("Content Store", "Stores audio, video, PDF and image content", "S3").apply {
      addTags(DATABASE_TAG)
      addTags(CLOUD_PLATFORM_TAG)
      addTags(SOFTWARE_AS_A_SERVICE_TAG)
    }

    val drupal = system.addContainer("Prisoner Content Hub CMS", "Content Management System for HMPPS Digital and prison staff to curate content for prisoners", "Drupal").apply {
      setUrl("https://github.com/ministryofjustice/prisoner-content-hub-backend")
      addTags(CLOUD_PLATFORM_TAG)
      uses(drupalDatabase, "MariaDB connection")
      uses(elasticSearchStore, "HTTPS REST API")
      uses(s3ContentStore, "HTTPS REST API")
    }

    contentHubFrontend = system.addContainer("Prisoner Content Hub frontend", "Prisoner-facing view of the Content Hub", "Node").apply {
      setUrl("https://github.com/ministryofjustice/prisoner-content-hub-frontend")
      addTags(CLOUD_PLATFORM_TAG)
      uses(drupal, "HTTPS Rest API")
      uses(elasticSearchStore, "HTTPS REST API")
    }

    val kibanaDashboard = system.addContainer("Kibana dashboard", "Feedback reports and analytics dashboard", "Kibana").apply {
      //setUrl("TODO")
      uses(elasticSearchStore, "HTTPS Rest API")
      addTags(CLOUD_PLATFORM_TAG)
    }

    /**
     * Users
     */
    
    model.addPerson("Feedback Reporter", "HMPPS Staff collating feedback for protection, product development and analytics").apply {
      uses(kibanaDashboard, "Extracts CSV files, views individual feedback responses, and analyses sentiment and statistics of feedback")
    };

    model.addPerson("Prisoner", "A prisoner over 18 years old, held in the public prison estate").apply {
      uses(contentHubFrontend, "Views videos, audio programmes, site updates, and rehabilitative material")
    };

    model.addPerson("Young Offender", "A person under 18, held in a Young Offender Institute").apply {
      uses(contentHubFrontend, "Views videos, audio programmes, site updates, and rehabilitative material")
    };

    model.addPerson("Content editor", "HMPPS Digital staff curating content for the entire prison estate and supporting individual prisons").apply {
      uses(drupal, "Authors and curates content for the prison estate")
    };

    model.addPerson("Prison Content editor", "A content author on-site in a prison, authoring content for their prison").apply {
      uses(drupal, "Authors and curates content for their prison")
    };
  }
}
