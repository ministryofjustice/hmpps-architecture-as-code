package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

import uk.gov.justice.hmpps.architecture.shared.Tags

class PrisonerContentHub(model: Model) {
  val system: SoftwareSystem
  val contentHubFrontend: Container

  companion object { 
    const val SOFTWARE_AS_A_SERVICE = "SAAS";
  } 
  
  /**
   * TODO: add Prisoner internet infrastructure, AD
   * TODO: add BT PINS
   */

  init {
    val cloudPlatform = model.getDeploymentNodeWithName("Cloud Platform")
    val rds = cloudPlatform.getDeploymentNodeWithName("RDS")
    val s3 = cloudPlatform.getDeploymentNodeWithName("S3")
    val kubernetes = cloudPlatform.getDeploymentNodeWithName("Kubernetes")

    system = model.addSoftwareSystem(
      "Prisoner Content Hub", 
      """
      The Prisoner Content Hub is a platform for prisoners to access data, content and services supporting individual progression and freeing up staff time.
      """.trimIndent())

    val elasticSearchStore = system.addContainer("ElasticSearch store", "Data store for feedback collection, and indexing for Drupal CMS content", "ElasticSearch").apply {
      addTags(Tags.DATABASE.toString())
      addTags(Tags.SOFTWARE_AS_A_SERVICE.toString())
      cloudPlatform.add(this)
    }

    val drupalDatabase = system.addContainer("Drupal database", null, "MariaDB").apply {
      addTags(Tags.DATABASE.toString())
      addTags(Tags.SOFTWARE_AS_A_SERVICE.toString())
      rds.add(this)
    }

    val s3ContentStore = system.addContainer("Content Store", "Stores audio, video, PDF and image content", "S3").apply {
      addTags(Tags.DATABASE.toString())
      addTags(Tags.SOFTWARE_AS_A_SERVICE.toString())
      s3.add(this)
    }

    val drupal = system.addContainer("Prisoner Content Hub CMS", "Content Management System for HMPPS Digital and prison staff to curate content for prisoners", "Drupal").apply {
      setUrl("https://github.com/ministryofjustice/prisoner-content-hub-backend")
      uses(drupalDatabase, "MariaDB connection")
      uses(elasticSearchStore, "HTTPS REST API")
      uses(s3ContentStore, "HTTPS REST API")
      kubernetes.add(this)
    }

    contentHubFrontend = system.addContainer("Prisoner Content Hub frontend", "Prisoner-facing view of the Content Hub", "Node").apply {
      setUrl("https://github.com/ministryofjustice/prisoner-content-hub-frontend")
      uses(drupal, "HTTPS Rest API")
      uses(elasticSearchStore, "HTTPS REST API")
      kubernetes.add(this)
    }

    val kibanaDashboard = system.addContainer("Kibana dashboard", "Feedback reports and analytics dashboard", "Kibana").apply {
      //setUrl("TODO")
      uses(elasticSearchStore, "HTTPS Rest API")
      cloudPlatform.add(this)
    }

    /**
     * Users
     */
    
    model.addPerson("Feedback Reporter", "HMPPS Staff collating feedback for protection, product development and analytics").apply {
      uses(kibanaDashboard, "Extracts CSV files of prisoner feedback, views individual feedback responses, and analyses sentiment and statistics of feedback")
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
