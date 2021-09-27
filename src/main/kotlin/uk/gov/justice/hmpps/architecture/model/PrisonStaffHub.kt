package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class PrisonStaffHub private constructor() {

  companion object : HMPPSSoftwareSystem {
    lateinit var model: Model
    lateinit var system: SoftwareSystem
    lateinit var mainApp: Container
    lateinit var keyworkerUi: Container
    lateinit var restrictedPatientsUi: Container

    override fun defineModelEntities(model: Model) {
      Companion.model = model

      system = model.addSoftwareSystem(
        "Prison Staff Hub",
        """
        Gives Prison Staff access to the data and operation they need on a day-to-day basis. Contains a subset of the functionality in C-NOMIS.
        """.trimIndent()
      )

      keyworkerUi = system.addContainer("Manage Key Workers", "Web app that contains functionality related to key workers").apply {
        setUrl("https://github.com/ministryofjustice/manage-key-workers")
      }

      restrictedPatientsUi = system.addContainer("Manage Restricted Patients", "Web app that contains functionality related to restricted patients").apply {
        setUrl("https://github.com/ministryofjustice/hmpps-restricted-patients")
      }

      mainApp = system.addContainer("Prison Staff Hub", "The web app that contains the main features").apply {
        setUrl("https://github.com/ministryofjustice/prisonstaffhub")
        uses(keyworkerUi, "Provides links to")
        uses(restrictedPatientsUi, "Provides links to")
      }
    }

    override fun defineRelationships() {
      mainApp.uses(NOMIS.prisonApi, "lookup visits, canteen, etc.")
      mainApp.uses(NOMIS.offenderSearch, "Retrieves prisoner data from")
      mainApp.uses(HMPPSAuth.system, "HTTPS Rest API")
      mainApp.uses(TokenVerificationApi.api, "validates API tokens via", "HTTPS Rest API")
      mainApp.uses(WhereaboutsApi.api, "HTTPS Rest API")

      keyworkerUi.uses(NOMIS.prisonApi, "lookup visits, canteen, etc.")
      keyworkerUi.uses(HMPPSAuth.system, "HTTPS Rest API")
      keyworkerUi.uses(TokenVerificationApi.api, "validates API tokens via", "HTTPS Rest API")
      keyworkerUi.uses(KeyworkerApi.api, "HTTPS Rest API")
      keyworkerUi.uses(ComplexityOfNeed.mainApp, "retrieves complexity of needs information from")

      restrictedPatientsUi.uses(NOMIS.prisonApi, "lookup visits, canteen, etc.")
      restrictedPatientsUi.uses(NOMIS.offenderSearch, "Retrieves prisoner data from")
      restrictedPatientsUi.uses(HMPPSAuth.system, "HTTPS Rest API")
      restrictedPatientsUi.uses(TokenVerificationApi.api, "validates API tokens via", "HTTPS Rest API")
      restrictedPatientsUi.uses(RestrictedPatientsApi.api, "HTTPS Rest API")
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
