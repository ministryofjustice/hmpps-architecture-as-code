package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class BookVideoLink private constructor() {

  companion object : HMPPSSoftwareSystem {
    lateinit var model: Model
    lateinit var system: SoftwareSystem
    lateinit var bookVideoLinkService: Container

    lateinit var vccStaff: Person
    lateinit var omuStaff: Person
    lateinit var courtStaff: Person

    override fun defineModelEntities(model: Model) {
      this.model = model

      system = model.addSoftwareSystem(
        "Book Video Link",
        """
        The Book video link service allows court users to book video conferencing rooms inside a prison to allow Prisoners to attend court sessions.
        """.trimIndent()
      ).apply {
        val PRISON_PROBATION_PROPERTY_NAME = "business_unit"
        val PRISON_SERVICE = "prisons"
        addProperty(PRISON_PROBATION_PROPERTY_NAME, PRISON_SERVICE)
      }

      bookVideoLinkService = system.addContainer("Book video link service", "Allows courts to book, cancel and amend video calls with Prisoners", "Node").apply {
        setUrl("https://github.com/ministryofjustice/hmpps-book-video-link")

        CloudPlatform.kubernetes.add(this)
      }

      courtStaff = model.addPerson("Court bookings manager", "Court user who manages bookings").apply {
        uses(bookVideoLinkService, "Views, creates, requests and updates bookings ")
      }

      vccStaff = model.addPerson("VCC staff user", "Prison staff who manage the video conferencing suite")

      omuStaff = model.addPerson("OMU staff user", "Prison staff who works in the offender management unit")
    }

    override fun defineRelationships() {
      bookVideoLinkService.uses(NOMIS.prisonApi, "HTTPS Rest API")
      bookVideoLinkService.uses(HMPPSAuth.system, "HTTPS Rest API")
      bookVideoLinkService.uses(WhereaboutsApi.api, "HTTPS Rest API")
      bookVideoLinkService.uses(CourtRegister.api, "HTTPS Rest API")
      bookVideoLinkService.uses(TokenVerificationApi.api, "validates API tokens via", "HTTPS Rest API")
      bookVideoLinkService.uses(UserPreferenceApi.api, "Stores user's preferred courts in", "HTTPS Rest API")

      bookVideoLinkService.uses(Notify.system, "delivers notifications via")
      Notify.system.delivers(courtStaff, "emails booking/amendment/cancellation confirmations", "email")
      Notify.system.delivers(vccStaff, "emails requests/booking/amendment/cancellation notifications", "email")
      Notify.system.delivers(omuStaff, "emails booking/amendment/cancellation notifications", "email")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system, "bookVideoLinkSystemContext", "The system context diagram for the Book Video Link service"
      ).apply {
        addDefaultElements()

        remove(vccStaff)
        remove(omuStaff)

        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createSystemContextView(
        system, "bookVideoLinkUserRelationships", "Relationships between users and the Book Video Link service"
      ).apply {
        add(system)
        add(vccStaff)
        add(omuStaff)
        add(courtStaff)

        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "bookVideoLinkContainer", "Book Video Link service container view").apply {
        addDefaultElements()
        remove(vccStaff)
        remove(omuStaff)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(system, "bookVideoLinkContainerProductionDeployment", "The Production deployment scenario for the Book Video Link service").apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
