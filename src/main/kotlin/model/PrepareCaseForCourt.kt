package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import java.util.regex.Matcher

class PrepareCaseForCourt private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var prepareCaseUI: Container
    lateinit var courtCaseService: Container
    lateinit var courtCaseMatcher: Container

    override fun defineModelEntities(model: Model) {

      system = model.addSoftwareSystem("Prepare a Case for Court", "Digital Service for Probation Officers working in magistrates' courts, " +
        "providing them with a single location to access the defendant information they need to provide sound and timely sentencing guidance to magistrates")
        .apply {
          setLocation(Location.Internal)
        }

      val casesDb = system.addContainer(
        "Cases Database",
        "Holds court lists, cases and offender ids",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
      }

      val messagesDb = system.addContainer(
        "Messages Database",
        "Holds CPMG messages data and meta-data",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        Tags.SOFTWARE_AS_A_SERVICE.addTo(this)
      }

      courtCaseService = system.addContainer(
        "Court Case Service",
        "Court case business logic and REST API consumed by the Prepare a Case web application",
        "Java + Spring Boot"
      ).apply {
        uses(casesDb, "connects to", "JDBC")
      }

      courtCaseMatcher = system.addContainer(
        "Court Case Matcher",
        "Consumes court lists from Crime Portal and matches defendants  against the nDelius database via Offender Search",
        "Java + Spring Boot"
      ).apply {
        uses(casesDb, "connects to", "JDBC")
        uses(courtCaseService, "Creates or updates cases in")
      }

      prepareCaseUI = system.addContainer(
        "Prepare a Case UI",
        "Web application  which delivers  HTML to the users browser",
        "Node + Express"
      ).apply {
        uses(courtCaseService, "View case defendant details and capture court judgements using ")
      }

      val crimePortalMirrorGateway = system.addContainer(
        "Crime Portal Mirror Gateway",
        "OAP web service which receives court lists and publishes onto a JMS message queue",
        "JBoss Wildfly"
      ).apply {
        uses(messagesDb, "connects to", "JDBC")
        uses(courtCaseMatcher, "Sends court lists to")
      }

      // TODO refactor out HMCTS Crime Portal into dedicated SoftwareSystem
      model.addSoftwareSystem("HMCTS Crime Portal", "Case Management for HMCTS holding data relating to court cases")
        .apply {
          setLocation(Location.External)
          Tags.PROVIDER.addTo(this)
          uses(crimePortalMirrorGateway, "Sends court lists to")
        }
    }

    override fun defineRelationships() {
      listOf(prepareCaseUI, courtCaseService, courtCaseService)
        .forEach { it.uses(HMPPSAuth.system, "authenticates via") }

      courtCaseService.uses(Delius.communityApi, "Gets offender details from")
      courtCaseMatcher.uses(Delius.offenderSearch, "Matches defendants to known offenders")

      // TODO Model OASys & Offender Assessment API
//      courtCaseService.uses(OASys.offenderAssessmentApi, "Gets offender assessment details from")
    }

    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(system, "prepare-case-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "prepare-case-container", null).apply {
        addDefaultElements()
        addAllContainers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
