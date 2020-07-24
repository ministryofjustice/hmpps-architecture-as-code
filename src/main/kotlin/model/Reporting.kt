package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class Reporting private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var ndmis: SoftwareSystem

    lateinit var dataInnovationTeam: Person
    lateinit var nationalApplicationReportingTeam: Person

    lateinit var npsPerformanceOfficer: Person
    lateinit var prisonPerformanceTeam: Person
    lateinit var communityPerformanceTeam: Person
    lateinit var crcPerformanceAnalyst: Person

    override fun defineModelEntities(model: Model) {
      ndmis = model.addSoftwareSystem(
        "NDMIS",
        "National Delius Management Information System,\nprovides reporting on nDelius data"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      communityPerformanceTeam = model.addPerson("Community Performance team", "Reporting on HMPPS performance in the community")
      crcPerformanceAnalyst = model.addPerson("CRC performance analyst").apply { Tags.PROVIDER.addTo(this) }
      dataInnovationTeam = model.addPerson("Data Innovation, Analysis and Linking team", "Works on linked data from various non-HMPPS government departments")
      nationalApplicationReportingTeam = model.addPerson("NART", "National Applications Reporting Team,\nResponsible for the delivery of reporting to stakeholders")
      npsPerformanceOfficer = model.addPerson("NPS performance and quality officer")
      prisonPerformanceTeam = model.addPerson("Prison Performance team", "Reporting on HMPPS performance in prison")

      listOf(communityPerformanceTeam, prisonPerformanceTeam, dataInnovationTeam)
        .forEach { it.addProperty("org", "DASD") }
    }

    override fun defineRelationships() {
      ndmis.uses(Delius.system, "extracts and transforms data from")
      communityPerformanceTeam.uses(Reporting.ndmis, "uses reports in")
      crcPerformanceAnalyst.uses(Reporting.ndmis, "uses reports in")
      dataInnovationTeam.uses(Reporting.ndmis, "uses data from")
      nationalApplicationReportingTeam.uses(Reporting.ndmis, "creates reports in")
      npsPerformanceOfficer.uses(Reporting.ndmis, "uses reports in")
      prisonPerformanceTeam.uses(Reporting.ndmis, "to provide details of offenders released into the community, looks into")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(ndmis, "ndmis-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 200, 200)
      }
    }
  }
}
