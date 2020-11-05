package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.OutsideHMPPS
import uk.gov.justice.hmpps.architecture.annotations.Tags


class PrisonerMoney private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var sendMoneyService: Container
    lateinit var apiService: Container
    lateinit var bankAdminService: Container
    lateinit var nomsOpsService: Container
    lateinit var cashbookService: Container
    lateinit var transactionUploaderCronJob: Container
    lateinit var sendMoneyPublicUser: Person
    lateinit var fiuUser: Person
    lateinit var intelligenceUser: Person
    lateinit var businessHubUser: Person
    lateinit var ssclUser: Person
    lateinit var natwestFtp: SoftwareSystem
    lateinit var govpay: SoftwareSystem
    lateinit var worldpay: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Prisoner Money",
        "Prisoner Money"
      )

      // External Services
      natwestFtp = model.addSoftwareSystem("Natwest", "FTP server within natwest into which they place bank transaction records in propriatory bankline format")
        .apply {
          setLocation(Location.External)
          OutsideHMPPS.addTo(this)
        }
      worldpay = model.addSoftwareSystem("worldpay", "WorldPay payment service")
        .apply {
          OutsideHMPPS.addTo(this)
        }
      govpay = model.addSoftwareSystem("Gov.Pay", "Centralized goverment payment portal")
        .apply {
          OutsideHMPPS.addTo(this)
        }

      // Prisoner Money Services
      val db = system.addContainer(
        "Prisoner Money Database",
        "Prisoner Money API Persistence Layer", "RDS Postgres DB"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      apiService = system.addContainer(
        "Prisoner Money API",
        "API over the Prisoner Money DB used by internal applications", "Django App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/money-to-prisoners-api")
        uses(db, "psyCopg2 remote connection")
        CloudPlatform.kubernetes.add(this)
      }

      sendMoneyService = system.addContainer(
        "Prisoner Money Send Money to a Prisoner Service",
        "Web application to allow members of the public to send money to prisoners via debit card", "Django App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/money-to-prisoners-send-money")
        Tags.WEB_BROWSER.addTo(this)
        uses(apiService, "to validate prisoner details, and persist payment information")
        CloudPlatform.kubernetes.add(this)
      }

      bankAdminService = system.addContainer(
        "Prisoner Money Bank Admin",
        "Portal for SSCL staff to interact with Prisoner Money Services", "Django App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/money-to-prisoners-bank-admin")
        Tags.WEB_BROWSER.addTo(this)
        uses(apiService, "to retrieve information relating to bank transactions and disbersements")
        CloudPlatform.kubernetes.add(this)
      }

      cashbookService = system.addContainer(
        "Prisoner Money Digital Cashbook",
        "Portal for Prison Business Hub users to interact with Prisoner Money services", "Django App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/money-to-prisoners-cashbook")
        Tags.WEB_BROWSER.addTo(this)
        uses(apiService, "to retrieve and modify credits and retrieve prisoner information")
        CloudPlatform.kubernetes.add(this)
      }

      nomsOpsService = system.addContainer(
        "Prisoner Money Intelligence Tool Service",
        "Portal for prison Intelligence officers and HMPPS Financial Investigation Unit to interact with Prisoner Money Services", "Django App"
      ).apply {
        setUrl("https://github.com/ministryofjustice/money-to-prisoners-noms-ops")
        Tags.WEB_BROWSER.addTo(this)
        uses(apiService, "to retrieve and persist metadata relating to prisoners, senders and credits")
        CloudPlatform.kubernetes.add(this)
      }

      transactionUploaderCronJob = system.addContainer(
        "Prisoner Money Transaction Uploader",
        "Cron job that fetches the previous days bank transaction records from natwest FTP server", "Python Script"
      ).apply {
        setUrl("https://github.com/ministryofjustice/money-to-prisoners-transaction-uploader")
        uses(apiService, "to update bank tranasction information")
        uses(natwestFtp, "Downloads transaction records from FTP server")
        CloudPlatform.kubernetes.add(this)
      }

      // Users
      sendMoneyPublicUser = model.addPerson("Send Money User", "Any member of the public with a debit card, and the prisoner number and date of birth for a particular prisoner").apply {
        OutsideHMPPS.addTo(this)
      }
      fiuUser = model.addPerson("Financial Investigation Unit User", "A member of the HMPPS Financial Investigations Unit team, ")
      intelligenceUser = model.addPerson("Intelligence User", "Direct employee of HMPPS or a contracted prison and working in an intelligence function")
      businessHubUser = model.addPerson("Business Hub User", "Employee within the Business Hub unit of a public or private prison")
      ssclUser = model.addPerson(
        "SSCL User",
        "Employee of Shared Services Ltd"
      ).apply {
        OutsideHMPPS.addTo(this)
      }
    }
    override fun defineRelationships() {
      sendMoneyPublicUser.uses(sendMoneyService, "Visits send money public site to transfer money from their debit card to the Canteen account of someone in prison" , "HTTPS")
      fiuUser.uses(nomsOpsService, "Views uncaptured payments flagged as suspicious and either approves it or cancels it", "HTTPS")
      intelligenceUser.uses(nomsOpsService, "View credits relating to a prisoner in one or more prisons", "HTTPS")
      businessHubUser.uses(cashbookService, "Credit money into a prisoners account or send money out of that account as a disbursement", "HTTPS")
      ssclUser.uses(bankAdminService, "Downloads ADI journals, bank statements and disbersements", "HTTPS")
      apiService.uses(NOMIS.prisonApi, "To fetch credit totals, location, and transaction history for a prisoner")
      cashbookService.uses(NOMIS.prisonApi, "To create transactions to credit monies to a prisoner")
      nomsOpsService.uses(NOMIS.prisonApi, "To fetch photograph of a prisoner")

      govpay.uses(worldpay, "to capture monies from a given debit card", "HTTPS")
      govpay.uses(sendMoneyService, "to pass on information from a successful transaction", "HTTPS")
      sendMoneyService.uses(govpay, "to authorize the transfer of money from the debit card on behalf of the user", "HTTPS")
    }
    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(system, "prisoner-money-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "prisoner-money-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
