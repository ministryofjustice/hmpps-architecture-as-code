package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class Pathfinder(model: Model) {
    val system: SoftwareSystem
    val webApp: Container

    init {
        val pathfinder = model.addSoftwareSystem("Pathfinder", """
    Pathfinder System,
    the case management system for Pathfinder nominals
    """.trimIndent())

        val db = pathfinder.addContainer("Pathfinder Database", "Database to store Pathfinder case management", "RDS Postgres DB").apply {
            addTags("database")
        }

        webApp = pathfinder.addContainer("Pathfinder Web Application", "Web application for the case management of Pathfinder nominals", "Node Express app").apply {
            addTags("WebBrowser")
            uses(db, null)
        }

        pathfinder.addContainer("Pathfinder API", "API over the Pathfinder DB used by internal applications", "Kotlin Spring Boot App").apply {
            setUrl("https://github.com/ministryofjustice/pathfinder-api")
            uses(db, "JDBC")
        }

        val pPrisonPreventLead = model.addPerson("Prison Prevent Lead", "They case manage Pathfinder Nominals in custody")
        pPrisonPreventLead.uses(pathfinder, null)
        pPrisonPreventLead.uses(webApp, "Visits pathfinder.service.justice.gov.uk using", "HTTPS")

        val pRegionalPrisonPreventLead = model.addPerson("Regional Prison Prevent Lead", "They case manage a region of Pathfinder Nominals in custody")
        pRegionalPrisonPreventLead.uses(pathfinder, null)
        pRegionalPrisonPreventLead.uses(webApp, "Visits pathfinder.service.justice.gov.uk using", "HTTPS")

        val pProbationOffenderManager = model.addPerson("Probation Offender Manager", "They case manage Pathfinder Nominals in the community")
        pProbationOffenderManager.uses(pathfinder, null)
        pProbationOffenderManager.uses(webApp, "Visits pathfinder.service.justice.gov.uk using", "HTTPS")

        val pRegionalPoliceUser = model.addPerson("Regional Police User", "They access limited regional data of Pathfinder Nominals")
        pRegionalPoliceUser.uses(pathfinder, null)
        pRegionalPoliceUser.uses(webApp, "Visits pathfinder.service.justice.gov.uk using", "HTTPS")

        val pNationalPoliceUser = model.addPerson("National Police User", "They access limited National data of Pathfinder Nominals")
        pNationalPoliceUser.uses(pathfinder, null)
        pNationalPoliceUser.uses(webApp, "Visits pathfinder.service.justice.gov.uk using", "HTTPS")

        val pNationalIntelligenceUnitUser = model.addPerson("National Prison User", "They have access to all Pathfinder nominals")
        pNationalIntelligenceUnitUser.uses(pathfinder, null)
        pNationalIntelligenceUnitUser.uses(webApp, "Visits pathfinder.service.justice.gov.uk using", "HTTPS")

        system = pathfinder
    }
}
