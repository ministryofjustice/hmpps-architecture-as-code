package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class OASys private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var assessmentsApi: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("Offender Assessment System", "(OASys) Assesses the risks and needs of offenders").apply {
        setUrl("https://dsdmoj.atlassian.net/wiki/spaces/~474366104/pages/2046820357/OASys+Overview")
      }

      val oracleApp = system.addContainer("Case Management System", "Assesses the risks and needs of offenders", "Oracle APEX")

      assessmentsApi = system.addContainer("Offender Assessments API", "REST access to the OASYS Oracle DB offender assessment information", "Kotlin + Spring Boot").apply {
        setUrl("https://github.com/ministryofjustice/offender-assessments-api-kotlin")
        APIDocs("https://offender-dev.aks-dev-1.studio-hosting.service.justice.gov.uk/swagger-ui.html").addTo(this)
      }

      val db = system.addContainer("Assessments Database", null, "Oracle")

      oracleApp.uses(db, "connects to")
      assessmentsApi.uses(db, "connects to", "JDBC")
    }

    override fun defineRelationships() {
      system.uses(Delius.system, "assessment info, risk measures are copied into", "NDH")
      ProbationPractitioners.nps.uses(system, "records offender risk (attendance, contact, etc.) and assessment in")
      ProbationPractitioners.crc.uses(system, "records offender risk (attendance, contact, etc.) and assessment in")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
