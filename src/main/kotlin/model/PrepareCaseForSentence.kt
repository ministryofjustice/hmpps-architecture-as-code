package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.OutsideHMPPS
import uk.gov.justice.hmpps.architecture.annotations.Tags

class PrepareCaseForSentence private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var prepareCaseUI: Container
    lateinit var courtCaseService: Container
    lateinit var courtCaseMatcher: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Prepare a Case for Sentence",
        "Digital Service for Probation Officers working in magistrates' courts, " +
          "providing them with a single location to access the defendant information " +
          "they need to provide sound and timely sentencing guidance to magistrates"
      )

      val casesDb = system.addContainer(
        "Cases Database",
        "Holds court lists, cases and offender ids",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      val messagesBucket = system.addContainer(
        "Message Store",
        "Holds CPG messages data and meta-data",
        "S3"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.s3.add(this)
      }

      val crimePortalGatewayQueue = system.addContainer(
        "crime-portal-gateway-queue",
        "Carries court list messages",
        "SQS"
      ).apply {
        Tags.QUEUE.addTo(this)
        CloudPlatform.sqs.add(this)
      }

      courtCaseService = system.addContainer(
        "Court Case Service",
        "Court case business logic and REST API consumed by the Prepare a Case web application",
        "Java + Spring Boot"
      ).apply {
        uses(casesDb, "connects to", "JDBC")
        setUrl("https://github.com/ministryofjustice/court-case-service")
      }

      courtCaseMatcher = system.addContainer(
        "Court Case Matcher",
        "Consumes court lists from Crime Portal and matches defendants  against the nDelius database via Offender Search",
        "Java + Spring Boot"
      ).apply {
        uses(courtCaseService, "Creates or updates cases in")
        uses(crimePortalGatewayQueue, "Consumes court list messages from")
        setUrl("https://github.com/ministryofjustice/court-case-matcher")
      }

      prepareCaseUI = system.addContainer(
        "Prepare a Case UI",
        "Web application  which delivers  HTML to the users browser",
        "Node + Express"
      ).apply {
        uses(courtCaseService, "View case defendant details and capture court judgements using ")
        setUrl("https://github.com/ministryofjustice/prepare-a-case")
      }

      val crimePortalGateway = system.addContainer(
        "Crime Portal Gateway",
        "SOAP web service which receives court lists from HMCTS and publishes onto an SQS message queue",
        "Kotlin + Spring Boot"
      ).apply {
        uses(crimePortalGatewayQueue, "Produces court list messages to")
        uses(messagesBucket, "stores messages in")
        setUrl("https://github.com/ministryofjustice/crime-portal-gateway")
      }

      // TODO refactor out HMCTS Crime Portal into dedicated SoftwareSystem
      model.addSoftwareSystem("HMCTS Crime Portal", "Case Management for HMCTS holding data relating to court cases")
        .apply {
          setLocation(Location.External)
          OutsideHMPPS.addTo(this)
          uses(crimePortalGateway, "Sends court lists to")
        }
    }

    override fun defineRelationships() {
      prepareCaseUI.uses(HMPPSAuth.system, "authenticates via")
      listOf(courtCaseService, courtCaseMatcher)
        .forEach { it.uses(HMPPSAuth.system, "validates API tokens via") }

      courtCaseService.uses(Delius.communityApi, "Gets offender details from")
      courtCaseMatcher.uses(Delius.offenderSearch, "Matches defendants to known offenders")
      courtCaseService.uses(OASys.assessmentsApi, "get offender assessment details from")

      prepareCaseUI.uses(UserPreferenceApi.api, "Stores user's preferred courts in", "HTTPS Rest API")

      CourtUsers.courtAdministrator.uses(prepareCaseUI, "prepares cases for sentencing")
      ProbationPractitioners.nps.uses(prepareCaseUI, "views case defendant details")
    }

    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(system, "prepare-case-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "prepare-case-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
