package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Delius private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var database: Container
    lateinit var communityApi: Container
    lateinit var offenderSearch: Container
    lateinit var offenderSearchIndexer: Container
    lateinit var offenderElasticsearchStore: Container
    lateinit var supportTeam: Person
    lateinit var deliusApi: Container
    lateinit var probationOffenderEvents: Container

    override fun defineModelEntities(model: Model) {

      val deliusAWSAccount = AWS.london.addDeploymentNode("Delius account")
      val ec2 = deliusAWSAccount.addDeploymentNode("EC2", "AWS Elastic Compute Cloud", "AWS")
      val ecs = deliusAWSAccount.addDeploymentNode("ECS", "AWS Elastic Container Service", "AWS")
      val batch = deliusAWSAccount.addDeploymentNode("Batch", "AWS Batch", "AWS")

      // People
      supportTeam = model.addPerson(
        "NDST",
        "(National Delius Support Team) Team supporting changes to data in National Delius"
      )

      // Top level NDelius system
      system = model.addSoftwareSystem(
        "NDelius",
        "(National Delius) Supporting the management of offenders and delivering national reporting and performance monitoring data"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      // NDelius database components
      database = system.addContainer(
        "NDelius database",
        "NPS supervision and case management data",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
        ec2.add(this)
      }

      // API services
      deliusApi = system.addContainer(
        "Delius API",
        "Write API over the NDelius DB used by HMPPS Digital team applications and services", "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/hmpps-delius-api"
        uses(database, "connects to", "JDBC")
        ecs.add(this)
      }

      communityApi = system.addContainer(
        "Community API",
        "API over the NDelius DB used by HMPPS Digital team applications and services", "Java"
      ).apply {
        url = "https://github.com/ministryofjustice/community-api"
        uses(database, "connects to", "JDBC")
        uses(deliusApi, "Writes data to NDelius using")
        ecs.add(this)
      }

      // Delius application components
      val documentStore = system.addContainer(
        "NDelius Document Store",
        "Storage of documents relating to NPS supervision and case management",
        "Alfresco"
      ).apply {
        ec2.add(this)
      }

      val userDirectory = system.addContainer(
        "NDelius User Directory",
        "Store of NDelius users and roles and provider of authentication services",
        "OpenLDAP"
      ).apply {
        ec2.add(this)
      }

      val ndelius = system.addContainer(
        "NDelius application",
        "Application logic for NDelius",
        "Java"
      ).apply {
        url = "https://github.com/ministryofjustice/delius"
        uses(database, "connects to", "JDBC")
        uses(documentStore, "stores and retrieves documents", "HTTPS")
        uses(userDirectory, "authenticates using", "LDAP")
        ecs.add(this)
      }

      val userManagement = system.addContainer(
        "NDelius User Management Tool",
        "NDelius user and role management",
        "Java"
      ).apply {
        uses(database, "connects to", "JDBC")
        uses(userDirectory, "authenticates using", "LDAP")
        ecs.add(this)
      }

      system.addContainer(
        "NextCloud",
        "NDelius shared file storage for accessing exported data etc.",
        "PHP Application"
      ).apply {
        uses(userDirectory, "authenticates using", "LDAP")
        ecs.add(this)
      }

      system.addContainer(
        "GDPR Service",
        "NDelius GDPR service",
        "Java"
      ).apply {
        uses(userManagement, "connects to", "HTTPS")
        uses(database, "connects to", "JDBC")
        ecs.add(this)
      }

      system.addContainer(
        "Merge Service",
        "NDelius Offender Merge service",
        "Java"
      ).apply {
        uses(userManagement, "connects to", "HTTPS")
        uses(database, "connects to", "JDBC")
        ecs.add(this)
      }

      system.addContainer(
        "Password Reset",
        "NDelius password reset tool",
        "Java"
      ).apply {
        uses(userDirectory, "updates credentials in", "LDAP")
        ecs.add(this)
      }

      system.addContainer(
        "OFFLOC Consumer/DSS Batch Job",
        "Download PNOMIS data and insert into NDelius database",
        "AWS Batch"
      ).apply {
        uses(ndelius, "Uses API exposed by", "HTTPS")
        batch.add(this)
      }

      // Domain events
      probationOffenderEvents = system.addContainer(
        "probation-offender-events topic",
        "Topic receiving notifications on core offender changes",
        "SNS topic"
      ).apply {
        Tags.TOPIC.addTo(this)
      }

      system.addContainer(
        "Probation Offender Events",
        "Generate events for the offender changes in probation",
        "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/probation-offender-events"
        uses(communityApi, "reads offender delta updates from")
        uses(probationOffenderEvents, "notifies", "SNS")
        CloudPlatform.kubernetes.add(this)
      }

      // Offender search
      offenderElasticsearchStore = system.addContainer(
        "Offender Elasticsearch store",
        "Data store for Delius offender data content", "Elasticsearch"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.elasticsearch.add(this)
      }

      offenderSearchIndexer = system.addContainer(
        "Probation Offender Search Indexer",
        "Service to update NDelius offender data held in Elasticsearch",
        "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/probation-offender-search-indexer"
        uses(offenderElasticsearchStore, "Indexes offender data from NDelius to the Elasticsearch Index")
        uses(probationOffenderEvents, "to know when to update a record, listens to offender changed events from", "SQS")
        uses(communityApi, "following received events, retrieves latest offender records from")
        CloudPlatform.kubernetes.add(this)
      }

      offenderSearch = system.addContainer(
        "Probation Offender Search API",
        "API over the NDelius offender data held in Elasticsearch",
        "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/probation-offender-search"
        uses(offenderElasticsearchStore, "Queries offender data from NDelius Elasticsearch Index")
        uses(offenderSearchIndexer, "To synchronise date from NDelius to Elasticsearch")
        CloudPlatform.kubernetes.add(this)
      }

      system.addContainer(
        "Probation Offender Search Webapp / PSR (New Tech)",
        "Search front end for NDelius offender data held in Elasticsearch / Pre-Sentence Report generation",
        "Java/Scala"
      ).apply {
        url = "https://github.com/ministryofjustice/ndelius-new-tech"
        uses(offenderSearch, "Searches offender data using")
        uses(documentStore, "Uploads document to", "HTTPS")
        uses(communityApi, "Accesses NDelius information using")
        ecs.add(this)
      }
    }

    override fun defineRelationships() {
      ProbationPractitioners.crc.uses(system, "records and reviews assessment decision, sentence plan in")
      ProbationPractitioners.nps.uses(system, "records and reviews assessment decision, sentence plan, pre-sentence report, referrals in")
      CourtUsers.courtAdministrator.uses(system, "records CAS decision, referrals in")

      system.uses(IM.system, "pushes active sentence requirements or licence conditions to", "IAPS")

      InterventionTeams.interventionServicesTeam.interactsWith(supportTeam, "raises task to create or update an accredited programme with")
      InterventionTeams.crcProgrammeManager.interactsWith(supportTeam, "raises task to update interventions with")
      supportTeam.uses(system, "updates interventions in")

      communityApi.uses(HMPPSAuth.system, "Authenticates using")
      deliusApi.uses(HMPPSAuth.system, "Authenticates using")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "delius-data-context", null).apply {
        addDefaultElements()
        model.people.map { remove(it) }
      }

      views.createSystemContextView(system, "delius-people-context", null).apply {
        addDefaultElements()
        model.softwareSystems.filter { it != system }.map { remove(it) }
      }

      views.createContainerView(system, "delius-container", null).apply {
        system.containers.map { add(it) }
      }

      views.createContainerView(system, "delius-container-integration-view", null).apply {
        add(deliusApi)
        add(communityApi)
        add(database)
        addNearestNeighbours(deliusApi)
        addNearestNeighbours(communityApi)
        remove(HMPPSAuth.system)
      }

      views.createDeploymentView(
        system,
        "delius-container-production-deployment",
        "The Production deployment scenario for Delius"
      ).apply {
        add(AWS.london)
      }
    }
  }
}
