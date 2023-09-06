package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.Notify
import uk.gov.justice.hmpps.architecture.annotations.Tags

class CreateAndVaryALicence private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ppcsCaseWorker: Person
    lateinit var createAndVaryALicenceUi: Container
    lateinit var createAndVaryALicenceApi: Container
    lateinit var gotenburg: Container
    lateinit var storage: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem(
        "Create And Vary A Licence",
        "A service to help create and manage licences."
      )

      db = system.addContainer(
        "Create And Vary A Licence API Database",
        "Database to store licence data",
        "RDS Postgres DB"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      gotenburg = system.addContainer(
        "Gotenburg PDF Generator",
        "Generates PDFs of licences from data",
        "Java"
      ).apply {
        CloudPlatform.kubernetes.add(this)
      }

      storage = system.addContainer(
        "Assessments storage",
        "Storage for PDFs of licences",
        "S3"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.s3.add(this)
      }

      createAndVaryALicenceApi = system.addContainer(
        "Create And Vary A Licence API",
        "REST API for the Create And Vary A Licence service",
        "Kotlin Spring Boot App"
      ).apply {
        Tags.DOMAIN_API.addTo(this)
        Tags.AREA_PROBATION.addTo(this)
        setUrl("https://github.com/ministryofjustice/create-and-vary-a-licence-api")
        CloudPlatform.kubernetes.add(this)
      }

      createAndVaryALicenceUi = system.addContainer(
        "Create And Vary A Licence Web Application",
        "Web application for the Create And Vary A Licence service",
        "Node Express app"
      ).apply {
        setUrl("https://github.com/ministryofjustice/create-and-vary-a-licence")
        Tags.WEB_BROWSER.addTo(this)
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      createAndVaryALicenceApi.uses(db, "queries", "JDBC")

      createAndVaryALicenceUi.uses(PrisonRegister.api, "gets prison details")
      createAndVaryALicenceUi.uses(NOMIS.offenderSearch, "searches for a prisoner")
      createAndVaryALicenceUi.uses(NOMIS.prisonApi, "gets sentence details")
      createAndVaryALicenceUi.uses(Delius.offenderSearch, "searches for offender")
      createAndVaryALicenceUi.uses(Delius.communityApi, "gets staff caseload")
      createAndVaryALicenceUi.uses(gotenburg, "creates pdfs")
      createAndVaryALicenceUi.uses(storage, "stores pdfs for later retrieval")

      createAndVaryALicenceApi.uses(HMPPSDomainEvents.topic, "publishes events to", "SNS")
      createAndVaryALicenceApi.uses(Notify.system, "sends notifications via")

      ProbationPractitioners.nps.uses(createAndVaryALicenceUi, "Create, vary and view licences")
      ProbationPractitioners.nps.uses(HMPPSAuth.system, "Authenticates via")

      createAndVaryALicenceUi.uses(createAndVaryALicenceApi, "operates on", "HTTPS")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "create-and-vary-a-licence-context",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(
        system,
        "create-and-vary-a-licence-container",
        null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(ConsiderARecall.system, "create-and-vary-a-licence-deployment", "Deployment overview of the Create And Vary A Licence service").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
