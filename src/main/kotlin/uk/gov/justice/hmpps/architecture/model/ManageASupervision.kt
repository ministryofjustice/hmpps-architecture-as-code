package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.OutsideHMPPS
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class ManageASupervision private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var serviceUser: Person
    lateinit var manageASupervisionUi: Container

    override fun defineModelEntities(model: Model) {
      serviceUser = model.addPerson("Service User", "A person").apply {
        OutsideHMPPS.addTo(this)
      }

      system = model.addSoftwareSystem(
        "Manage A Supervision",
        "Digital service to support probation practitioners in managing " +
          "probation supervisions, including viewing service user history " +
          "and booking appointments."
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
      }

      manageASupervisionUi = system.addContainer(
        "Manage A Supervision UI",
        "Microlith web application, displaying service user history and handling appointment booking",
        "Typescript, Node, NestJS, Express"
      ).apply {
        setUrl("https://github.com/ministryofjustice/hmpps-manage-supervisions")
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      ProbationPractitioners.nps.uses(manageASupervisionUi, "manages supervisions with")
      serviceUser.interactsWith(ProbationPractitioners.nps, "Meets with")

      manageASupervisionUi.uses(HMPPSAuth.system, "authenticates via")
      manageASupervisionUi.uses(Delius.communityApi, "Gets SU and past-offence details from")
      manageASupervisionUi.uses(AssessRisksAndNeeds.riskNeedsService, "Fetches risk and criminogenic need data from")

      manageASupervisionUi.delivers(ProbationPractitioners.nps, "sends booked appointment details to", "email via gov.uk notify")
      manageASupervisionUi.delivers(serviceUser, "sends appointment reminders to", "email and SMS via gov.uk notify")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(
        system,
        "manage-a-supervision-context", null
      ).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "manage-a-supervision-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(system, "manage-a-supervision-deployment", "The Production deployment scenario for the Manage A Supervision service").apply {
        add(AWS.london)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDynamicView(system, "manage-a-supervision-dynamic", "Data flow for Manage a Supervision").apply {
        // Login
        add(ProbationPractitioners.nps, "Submits login credentials to", HMPPSAuth.system)
        add(HMPPSAuth.system, "Provides authentication token to", ProbationPractitioners.nps)
        add(ProbationPractitioners.nps, "Authenticates future actions with token", manageASupervisionUi)
        // Display profile
        add(ProbationPractitioners.nps, "Selects a service user", manageASupervisionUi)
        add(manageASupervisionUi, "fetches service user personal details", Delius.communityApi)
        add(manageASupervisionUi, "fetches service user contact history", Delius.communityApi)
        add(manageASupervisionUi, "fetches service user risk scores/flags", AssessRisksAndNeeds.riskNeedsService)
        add(manageASupervisionUi, "displays service user profile to", ProbationPractitioners.nps)
        // Booking appointments
        add(ProbationPractitioners.nps, "Enters appointment details", manageASupervisionUi)
        add(manageASupervisionUi, "creates individual appointment records (contacts) with", Delius.communityApi)
        add(manageASupervisionUi, "sends notification to", ProbationPractitioners.nps)
        add(manageASupervisionUi, "sends notification to", serviceUser)
        // Recording outcomes
        add(ProbationPractitioners.nps, "Records appointment outcome", manageASupervisionUi)
        add(manageASupervisionUi, "updates contact in NDelius via", Delius.communityApi)
      }
    }
  }
}
