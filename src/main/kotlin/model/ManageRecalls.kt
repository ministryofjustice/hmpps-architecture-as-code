package model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.CloudPlatform
import uk.gov.justice.hmpps.architecture.HMPPSAuth
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.NOMIS
import uk.gov.justice.hmpps.architecture.annotations.Tags
import uk.gov.justice.hmpps.architecture.model.PrisonRegister

class ManageRecalls private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ppcsCaseWorker: Person
    lateinit var manageRecallsUiContainer: Container
    lateinit var manageRecallsApiContainer: Container
    lateinit var dbContainer: Container

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "Manage Recalls",
        "The case management system for recalls"
      )

      dbContainer = system.addContainer(
        "Manage Recalls API Database",
        "Database to store recalls caseworking info",
        "RDS Postgres DB"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      manageRecallsApiContainer = system.addContainer(
        "Manage Recalls API",
        "REST API for the case management of recalls",
        "Kotlin Spring Boot App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/manage-recalls-api")
        CloudPlatform.kubernetes.add(this)
        CloudPlatform.s3.add(this)
        CloudPlatform.elasticache.add(this)
      }

      manageRecallsUiContainer = system.addContainer(
        "Manage Recalls Web Application",
        "Web application for the case management of recalls",
        "Node Express app"
      ).apply {
        setUrl("https://github.com/ministryofjustice/manage-recalls-ui")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
        CloudPlatform.elasticache.add(this)
      }

      ppcsCaseWorker = model.addPerson("PPCS caseworker", "They case manage recalls")
    }

    override fun defineRelationships() {
      listOf(manageRecallsApiContainer, manageRecallsUiContainer)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }
      ppcsCaseWorker.uses(manageRecallsUiContainer, "visits", "HTTPS")
      ppcsCaseWorker.uses(HMPPSAuth.system, "logs in")
      manageRecallsApiContainer.uses(dbContainer, "queries", "JDBC")
      manageRecallsApiContainer.uses(PrisonRegister.api, "validates prisons", "HTTPS")
      manageRecallsUiContainer.uses(PrisonRegister.api, "gets prisons", "HTTPS")
      manageRecallsUiContainer.uses(NOMIS.offenderSearch, "searches for prisoners")
      manageRecallsUiContainer.uses(manageRecallsApiContainer, "operates on", "HTTPS")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "manage-recalls-context",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(
        system,
        "manage-recalls-container",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
