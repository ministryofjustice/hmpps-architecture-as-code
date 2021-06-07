package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.APIDocs

class OASys private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var arn: Container
    lateinit var assessmentsApi: Container
    lateinit var assessmentsEvents: Container
    lateinit var assessmentsUpdateApi: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("OASys", "(Offender Assessment System) Assesses the risks and needs of offenders").apply {
        setUrl("https://dsdmoj.atlassian.net/wiki/spaces/~474366104/pages/2046820357/OASys+Overview")
      }

      val oasysDB = system.addContainer("Assessments Database", null, "Oracle")
      val arnDB = system.addContainer("New risk and needs database", null, "PostgreSQL").apply {
        CloudPlatform.rds.add(this)
      }

      assessmentsApi = system.addContainer("Offender Assessments API", "REST access to the OASYS Oracle DB offender assessment information", "Kotlin + Spring Boot").apply {
        uses(oasysDB, "connects to", "JDBC")
        setUrl("https://github.com/ministryofjustice/offender-assessments-api-kotlin")
        APIDocs("https://offender-dev.aks-dev-1.studio-hosting.service.justice.gov.uk/swagger-ui.html").addTo(this)
      }

      arn = system.addContainer("Risk and needs API", "", "Kotlin + Spring Boot").apply {
        url = "https://github.com/ministryofjustice/hmpps-assessments-api"
        APIDocs("https://assess-risks-and-needs-dev.hmpps.service.justice.gov.uk/swagger-ui.html").addTo(this)
        CloudPlatform.kubernetes.add(this)
        uses(arnDB, "connects to", "JDBC")
      }

      assessmentsEvents = system.addContainer("Offender Assessment Events", "Pushes assessment events to SQS", "Kotlin + Spring Boot").apply {
        uses(oasysDB, "connects to", "JDBC")
        setUrl("https://github.com/ministryofjustice/offender-assessments-events")
      }

      assessmentsUpdateApi = system.addContainer(
        "Offender Updates Service",
        "Write API layer for OASys",
        "Kotlin + Spring Boot"
      ).apply {
        uses(oasysDB, "connects to", "JDBC")
        setUrl("https://github.com/ministryofjustice/offender-assessments-updates")
      }
    }

    override fun defineRelationships() {
      ProbationPractitioners.nps.uses(system, "records offender risk (attendance, contact, etc.) and assessment in")
      ProbationPractitioners.crc.uses(system, "records offender risk (attendance, contact, etc.) and assessment in")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "OASYS-context", null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "OASYS-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
