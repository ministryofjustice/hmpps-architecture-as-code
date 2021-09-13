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

class ManageRecalls private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ppcsCaseWorker: Person
    lateinit var manageRecallsUiContainer: Container
    lateinit var manageRecallsApiContainer: Container
    lateinit var dbContainer: Container
    lateinit var prisonsRegisterContainer: Container

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

      prisonsRegisterContainer = system.addContainer(
        "Prisons Register",
        "Provides a list of prisons",
        "Kotlin Spring Boot App"
      ).apply {
        CloudPlatform.kubernetes.add(this)
      }

      manageRecallsApiContainer = system.addContainer(
        "Manage Recalls API",
        "REST API for the case management of recalls",
        "Kotlin Spring Boot App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/manage-recalls-api")
        uses(dbContainer, "queries", "JDBC")
        uses(prisonsRegisterContainer, "validates prisons", "HTTPS")
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
        uses(NOMIS.offenderSearch, "searches for prisoners")
        uses(manageRecallsApiContainer, "operates on", "HTTPS")
        uses(prisonsRegisterContainer, "gets prisons", "HTTPS")
        CloudPlatform.kubernetes.add(this)
        CloudPlatform.elasticache.add(this)
      }

      ppcsCaseWorker = model.addPerson("PPCS caseworker", "They case manage recalls")
      ppcsCaseWorker.uses(manageRecallsUiContainer, "visits", "HTTPS")
      ppcsCaseWorker.uses(HMPPSAuth.system, "Login")
    }

    override fun defineRelationships() {
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
