package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Reporting private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var ndmis: SoftwareSystem
    lateinit var newInterventionsETL: Container
    lateinit var landingBucket: Container

    lateinit var dataInnovationTeam: Person
    lateinit var nationalApplicationReportingTeam: Person

    lateinit var npsPerformanceOfficer: Person
    lateinit var prisonPerformanceTeam: Person
    lateinit var communityPerformanceTeam: Person
    lateinit var crcPerformanceAnalyst: Person

    override fun defineModelEntities(model: Model) {
      ndmis = model.addSoftwareSystem(
        "NDMIS",
        "(National Delius Management Information System) Provides reporting on nDelius data"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      val database = ndmis.addContainer(
        "Probation reporting database",
        "Contains probation data transformed for reporting needs",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }

      landingBucket = ndmis.addContainer(
        "NDMIS ETL reporting landing bucket",
        "Collects daily snapshots of data from new services for hand-off to NDMIS (reporting)",
        "S3 bucket"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.s3.add(this)
      }

      newInterventionsETL = ndmis.addContainer(
        "Interventions ETL job",
        "Storage area where data ingestion for business reporting starts for new probation services",
        "SAP Business Objects Data Services (BODS)"
      ).apply {
        Tags.PLANNED.addTo(this)
        uses(landingBucket, "extracts and transforms data from")
        uses(database, "stores copied and transformed data in")
      }

      communityPerformanceTeam = model.addPerson("Community Performance team", "Reporting on HMPPS performance in the community")
      crcPerformanceAnalyst = model.addPerson("CRC performance analyst").apply { Tags.PROVIDER.addTo(this) }
      dataInnovationTeam = model.addPerson("Data Innovation, Analysis and Linking team", "Works on linked data from various non-HMPPS government departments")
      nationalApplicationReportingTeam = model.addPerson("NART", "(National Applications Reporting Team) Responsible for the delivery of reporting to stakeholders")
      npsPerformanceOfficer = model.addPerson("NPS performance and quality officer")
      prisonPerformanceTeam = model.addPerson("Prison Performance team", "Reporting on HMPPS performance in prison")

      listOf(communityPerformanceTeam, prisonPerformanceTeam, dataInnovationTeam)
        .forEach { it.addProperty("org", "DASD") }
    }

    override fun defineRelationships() {
      ndmis.uses(Delius.database, "extracts and transforms data from", "Change Data Capture")
      communityPerformanceTeam.uses(ndmis, "uses reports in")
      crcPerformanceAnalyst.uses(ndmis, "uses reports in")
      dataInnovationTeam.uses(ndmis, "uses data from")
      nationalApplicationReportingTeam.uses(ndmis, "creates reports in")
      npsPerformanceOfficer.uses(ndmis, "uses reports in")
      prisonPerformanceTeam.uses(ndmis, "to provide details of offenders released into the community, looks into")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(ndmis, "ndmis-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 200, 200)
      }
    }
  }
}
