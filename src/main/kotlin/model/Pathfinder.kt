
package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Pathfinder(model: Model) {
  val system: SoftwareSystem
  val webApp: Container

  init {
    system = model.addSoftwareSystem(
      "Pathfinder",
      "Pathfinder System,\nthe case management system for Pathfinder nominals"
    )

    val db = system.addContainer(
      "Pathfinder Database",
      "Database to store Pathfinder case management", "RDS Postgres DB"
    ).apply {
      Tags.DATABASE.addTo(this)
      CloudPlatform.rds.add(this)
    }

    webApp = system.addContainer(
      "Pathfinder Web Application",
      "Web application for the case management of Pathfinder nominals", "Node Express app"
    ).apply {
      Tags.WEB_BROWSER.addTo(this)
      uses(db, null)
      uses(NOMIS.prisonApi, "extract NOMIS offender data")
      uses(NOMIS.offenderSearch, "to search for prisoners")
      uses(Delius.communityApi, "extract nDelius offender data")
      uses(Delius.offenderSearch, "to search for offenders")
      CloudPlatform.kubernetes.add(this)
    }

    system.addContainer(
      "Pathfinder API",
      "API over the Pathfinder DB used by internal applications", "Kotlin Spring Boot App"
    ).apply {
      setUrl("https://github.com/ministryofjustice/pathfinder-api")
      uses(db, "JDBC")
      CloudPlatform.kubernetes.add(this)
    }

    val pPrisonPreventLead = model.addPerson("Prison Prevent Lead", "They case manage Pathfinder Nominals in custody")
    pPrisonPreventLead.uses(webApp, "Visits pathfinder app to manage their local prison(s) caseload", "HTTPS")
    pPrisonPreventLead.uses(HMPPSAuth.system, "Login")

    val pRegionalPrisonPreventLead = model.addPerson("Regional Prison Prevent Lead", "They case manage a region of Pathfinder Nominals in custody")
    pRegionalPrisonPreventLead.uses(webApp, "Visits pathfinder app to manage their Regional prisons caseload", "HTTPS")
    pRegionalPrisonPreventLead.uses(HMPPSAuth.system, "Login")

    val pProbationOffenderManager = model.addPerson("Probation Offender Manager", "They case manage Pathfinder Nominals in the community")
    pProbationOffenderManager.uses(webApp, "Visits pathfinder app to manage their community caseload", "HTTPS")
    pProbationOffenderManager.uses(HMPPSAuth.system, "Login")

    val pRegionalPoliceUser = model.addPerson("Regional Police User", "They access limited regional data of Pathfinder Nominals")
    pRegionalPoliceUser.uses(webApp, "Visits pathfinder app to manage offenders in their region due for release", "HTTPS")
    pRegionalPoliceUser.uses(HMPPSAuth.system, "Login")

    val pNationalPoliceUser = model.addPerson("National Police User", "They access limited National data of Pathfinder Nominals")
    pNationalPoliceUser.uses(webApp, "Visits pathfinder app to manage offenders nationally due for release", "HTTPS")
    pNationalPoliceUser.uses(HMPPSAuth.system, "Login")

    val pNationalIntelligenceUnitUser = model.addPerson("National Prison User", "They have access to all Pathfinder nominals")
    pNationalIntelligenceUnitUser.uses(webApp, "Visits pathfinder app to manage the performance of the service", "HTTPS")
    pNationalIntelligenceUnitUser.uses(HMPPSAuth.system, "Login")
  }
}
