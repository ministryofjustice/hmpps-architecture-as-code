package uk.gov.justice.hmpps.architecture.probation

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.shared.Tags

class Reporting private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var ndmis: SoftwareSystem

    lateinit var dataInnovation: Person
    lateinit var nart: Person

    lateinit var npsPerformance: Person
    lateinit var prisonPerformance: Person
    lateinit var communityPerformance: Person
    lateinit var crcPerformance: Person

    override fun defineModelEntities(model: Model) {
      ndmis = model.addSoftwareSystem(
        "NDMIS",
        "National Delius Management Information System,\nprovides reporting on nDelius data"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      communityPerformance = model.addPerson("Community Performance team", "Reporting on HMPPS performance in the community")
      crcPerformance = model.addPerson("CRC performance analyst").apply { Tags.PROVIDER.addTo(this) }
      dataInnovation = model.addPerson("Data Innovation, Analysis and Linking team", "Works on linked data from various non-HMPPS government departments")
      nart = model.addPerson("National Applications Reporting Team", "Responsible for the delivery of reporting to stakeholders")
      npsPerformance = model.addPerson("NPS performance and quality officer")
      prisonPerformance = model.addPerson("Prison Performance team", "Reporting on HMPPS performance in prison")

      listOf(communityPerformance, prisonPerformance, dataInnovation)
        .forEach { it.addProperty("org", "DASD") }
    }

    override fun defineRelationships() {
      ndmis.uses(Delius.system, "extracts and transforms data from")
      communityPerformance.uses(Reporting.ndmis, "uses reports in")
      crcPerformance.uses(Reporting.ndmis, "uses reports in")
      dataInnovation.uses(Reporting.ndmis, "uses data from")
      nart.uses(Reporting.ndmis, "creates reports in")
      npsPerformance.uses(Reporting.ndmis, "uses reports in")
      prisonPerformance.uses(Reporting.ndmis, "to provide details of offenders released into the community, looks into")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(ndmis, "ndmis-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 200, 200)
      }
    }
  }
}
