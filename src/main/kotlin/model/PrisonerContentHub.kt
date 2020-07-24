package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class PrisonerContentHub private constructor() {

  companion object : HMPPSSoftwareSystem {
    lateinit var model: Model
    lateinit var system: SoftwareSystem
    lateinit var contentHubFrontend: Container

    /**
     * TODO: add Prisoner internet infrastructure, AD
     * TODO: add BT PINS
     **/
    override fun defineModelEntities(model: Model) {
      this.model = model

      system = model.addSoftwareSystem(
        "Prisoner Content Hub",
        """
        The Prisoner Content Hub is a platform for prisoners to access data, content and services supporting individual progression and freeing up staff time.
        """.trimIndent()
      ).apply {
        setLocation(Location.Internal)
        val PRISON_PROBATION_PROPERTY_NAME = "business_unit"
        val PRISON_SERVICE = "prisons"
        addProperty(PRISON_PROBATION_PROPERTY_NAME, PRISON_SERVICE)
      }

      val elasticSearchStore = system.addContainer("ElasticSearch store", "Data store for feedback collection, and indexing for Drupal CMS content", "ElasticSearch").apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.elasticsearch.add(this)
      }

      val drupalDatabase = system.addContainer("Drupal database", "Prisoner Content Hub CMS data and Drupal metadata", "MariaDB").apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      val s3ContentStore = system.addContainer("Content Store", "Audio, video, PDF and image content for the Prisoner Content Hub", "S3").apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.s3.add(this)
      }

      val drupal = system.addContainer("Prisoner Content Hub CMS", "Content Management System for HMPPS Digital and prison staff to curate content for prisoners", "Drupal").apply {
        setUrl("https://github.com/ministryofjustice/prisoner-content-hub-backend")
        uses(drupalDatabase, "MariaDB connection")
        uses(elasticSearchStore, "HTTPS REST API")
        uses(s3ContentStore, "HTTPS REST API")
        CloudPlatform.kubernetes.add(this)
      }

      contentHubFrontend = system.addContainer("Prisoner Content Hub frontend", "Prisoner-facing view of the Content Hub", "Node").apply {
        setUrl("https://github.com/ministryofjustice/prisoner-content-hub-frontend")
        uses(drupal, "HTTPS Rest API")
        uses(elasticSearchStore, "HTTPS REST API")
        CloudPlatform.kubernetes.add(this)
      }

      val kibanaDashboard = system.addContainer("Kibana dashboard", "Feedback reports and analytics dashboard", "Kibana").apply {
        // setUrl("TODO")
        uses(elasticSearchStore, "HTTPS Rest API")
        CloudPlatform.elasticsearch.add(this)
      }

      /**
       * Users
       **/
      model.addPerson("Feedback Reporter", "HMPPS Staff collating feedback for protection, product development and analytics").apply {
        uses(kibanaDashboard, "Extracts CSV files of prisoner feedback, views individual feedback responses, and analyses sentiment and statistics of feedback")
        setLocation(Location.Internal)
      }

      model.addPerson("Prisoner", "A prisoner over 18 years old, held in the public prison estate").apply {
        uses(contentHubFrontend, "Views videos, audio programmes, site updates, and rehabilitative material")
        setLocation(Location.External)
      }

      model.addPerson("Young Offender", "A person under 18, held in a Young Offender Institute").apply {
        uses(contentHubFrontend, "Views videos, audio programmes, site updates, and rehabilitative material")
        setLocation(Location.External)
      }

      model.addPerson("Content editor", "HMPPS Digital staff curating content for the entire prison estate and supporting individual prisons").apply {
        uses(drupal, "Authors and curates content for the prison estate")
        setLocation(Location.Internal)
      }

      model.addPerson("Prison Content editor", "A content author on-site in a prison, authoring content for their prison").apply {
        uses(drupal, "Authors and curates content for their prison")
        setLocation(Location.Internal)
      }
    }

    override fun defineRelationships() {
      contentHubFrontend.uses(NOMIS.prisonApi, "lookup visits, canteen, etc.")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system, "prisonerContentHubSystemContext", "The system context diagram for the Prisoner Content Hub"
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "prisonerContentHubContainer", "Prisoner Content Hub container view").apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(system, "prisonerContentHubContainerProductionDeployment", "The Production deployment scenario for the Prisoner Content Hub").apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
