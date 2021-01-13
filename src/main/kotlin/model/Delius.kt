package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Delius private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var communityApi: Container
    lateinit var offenderSearch: Container
    lateinit var offenderSearchIndexer: Container
    lateinit var supportTeam: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "nDelius",
        "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      val deliusAWSAccount = AWS.london.addDeploymentNode("Delius account")
      val ec2 = deliusAWSAccount.addDeploymentNode("EC2", "AWS Elastic Compute Cloud", "AWS")
      val elasticSearchDeployment = deliusAWSAccount.addDeploymentNode("ElasticSearch", "AWS ElasticSearch Service", "AWS")

      supportTeam = model.addPerson(
        "NDST",
        "(National Delius Support Team) Team supporting changes to data in National Delius"
      )

      val db = system.addContainer("nDelius database", null, "Oracle").apply {
        Tags.DATABASE.addTo(this)
        ec2.add(this)
      }

      val elasticSearchStore = system.addContainer(
        "ElasticSearch store",
        "Data store for Delius content", "ElasticSearch"
      ).apply {
        Tags.DATABASE.addTo(this)
        elasticSearchDeployment.add(this)
      }

      communityApi = system.addContainer(
        "Community API",
        "API over the nDelius DB used by HMPPS Digital team applications and services", "Java"
      ).apply {
        url = "https://github.com/ministryofjustice/community-api"
        uses(db, "connects to", "JDBC")
        ec2.add(this)
      }

      val topic = system.addContainer(
        "probation-offender-events topic",
        "Topic receiving notifications on core offender changes",
        "SNS topic"
      ).apply {
        Tags.TOPIC.addTo(this)
      }

      system.addContainer(
        "Probation offender events",
        "Generate events for the offender changes in probation",
        "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/probation-offender-events"
        uses(communityApi, "reads offender delta updates from")
        uses(topic, "notifies", "SNS")
        ec2.add(this)
      }

      offenderSearchIndexer = system.addContainer(
        "Probation Offender Search Indexer",
        "Service to update nDelius offender data held in Elasticsearch",
        "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/probation-offender-search-indexer"
        uses(elasticSearchStore, "Indexes offender data from nDelius to the Elasticsearch Index")
        ec2.add(this)
      }

      offenderSearch = system.addContainer(
        "Probation Offender Search",
        "API over the nDelius offender data held in Elasticsearch",
        "Kotlin"
      ).apply {
        url = "https://github.com/ministryofjustice/probation-offender-search"
        uses(elasticSearchStore, "Queries offender data from nDelius Elasticsearch Index")
        uses(offenderSearchIndexer, "To synchronise date from nDelius to Elasticsearch")
        ec2.add(this)
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
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "delius-data-context", null).apply {
        addDefaultElements()
        model.people.forEach(this::remove)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createSystemContextView(system, "delius-people-context", null).apply {
        addDefaultElements()
        model.softwareSystems.filter { it != system }.forEach(this::remove)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
