package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class Delius private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var supportTeam: Person

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "nDelius",
        "National Delius\nSupporting the management of offenders and delivering national reporting and performance monitoring data"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      supportTeam = model.addPerson(
        "NDST",
        "(National Delius Support Team) Team supporting changes to data in National Delius"
      )

      val db = system.addContainer("nDelius database", null, "Oracle").apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
      }

      val elasticSearchStore = system.addContainer(
        "ElasticSearch store",
        "Data store for Delius content", "ElasticSearch"
      ).apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
      }

      system.addContainer(
        "Community API",
        "API over the nDelius DB used by HMPPS Digital team applications and services", "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/community-api")
        uses(db, "connects to", "JDBC")
      }

      system.addContainer(
        "OffenderSearch",
        "API over the nDelius offender data held in Elasticsearch", "Java"
      ).apply {
        uses(elasticSearchStore, "Queries offender data from nDelius Elasticsearch Index")
      }
    }

    override fun defineRelationships() {
      IM.system.uses(system, "pushes service user contact information to", "IAPS")
      ProbationPractitioners.crc.uses(system, "records and reviews assessment decision, sentence plan in")
      ProbationPractitioners.nps.uses(system, "records and reviews assessment decision, sentence plan, pre-sentence report, referrals in")

      InterventionTeams.interventionServicesTeam.interactsWith(supportTeam, "raises task to create or update an accredited programme with")
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
