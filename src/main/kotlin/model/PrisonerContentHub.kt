package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.OutsideHMPPS
import uk.gov.justice.hmpps.architecture.annotations.Tags

class PrisonerContentHub private constructor() {

  companion object : HMPPSSoftwareSystem {
    lateinit var model: Model
    lateinit var system: SoftwareSystem
    lateinit var contentHubFrontend: Container
    lateinit var frontendProxy: Container

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
        val PRISON_PROBATION_PROPERTY_NAME = "business_unit"
        val PRISON_SERVICE = "prisons"
        addProperty(PRISON_PROBATION_PROPERTY_NAME, PRISON_SERVICE)
      }

      val elasticSearchStore = system.addContainer("ElasticSearch store", "Data store for feedback collection, and indexing for Drupal CMS content", "ElasticSearch").apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
        CloudPlatform.elasticsearch.add(this)
      }

      val googleDataStudio = system.addContainer("Google Data Studio", "Reporting tool for Google Analytics and Feedback data", "Google Data Studio").apply {
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
      }

      val feedbackUploaderJob = system.addContainer("Feedback uploader job", "Runs cron to collate daily feedback data", "TypeScript").apply {
        uses(elasticSearchStore, "Extracts feedback data")
        uses(googleDataStudio, "Uploads feedback")
        CloudPlatform.kubernetes.add(this)
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

      val googleAnalytics = system.addContainer("Google Analytics", "Tracking page views and user journeys", "Google Analytics").apply {
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
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
        uses(googleAnalytics, "pushes page events")
        CloudPlatform.kubernetes.add(this)
      }

      frontendProxy = system.addContainer("ICEcast streamer", "Proxy for media on external domains not accessible to prison users", "ICEcast").apply {
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
      model.addPerson("Content Hub Product Team", "Collates feedback for product development and analytics").apply {
        uses(googleDataStudio, "Views feedback entries")
        uses(googleAnalytics, "Views content hub frontend usage statistics")
      }

      model.addPerson("HMPPS local content manager", "Collates feedback for content and operational purposes").apply {
        uses(googleDataStudio, "Views report in Google Data Studio of prisoner feedback, views individual feedback responses, and analyses sentiment and statistics of feedback")
      }

      model.addPerson("Prisoner / Young Offender", "A person held in the public prison estate or a Young Offender Institute").apply {
        uses(contentHubFrontend, "Views videos, audio programmes, site updates, and rehabilitative material")
        uses(frontendProxy, "Listens to National Prison Radio live stream")
        uses(s3ContentStore, "Streams audio, video and uploaded media")
        OutsideHMPPS.addTo(this)
      }

      model.addPerson("Content editor", "HMPPS Digital staff curating content for the entire prison estate and supporting individual prisons").apply {
        uses(drupal, "Authors and curates content for the prison estate")
      }

      model.addPerson("Prison Content editor", "A content author on-site in a prison, authoring content for their prison").apply {
        uses(drupal, "Authors and curates content for their prison")
      }
    }

    override fun defineRelationships() {
      contentHubFrontend.uses(NOMIS.prisonApi, "lookup visits, canteen, etc.")
      contentHubFrontend.uses(DigitalPrisonsNetwork.identity, "Prisoner logs in with OAuth2 flow")

      frontendProxy.uses(NationalPrisonRadio.system, "proxy OGG livestream audio")
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
