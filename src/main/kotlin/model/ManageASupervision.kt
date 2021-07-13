package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.*
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ManageASupervision private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var manageASupervisionUi: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Manage A Supervision",
        "Digital Service for A, "
          + "B, " +
          "and C"
      )

      manageASupervisionUi = system.addContainer(
        "Manage A Supervision UI",
        "Web application, displaying service user history and handling appointment booking",
        "Typescript, Node, NestJS, Express"
      ).apply {
        setUrl("https://github.com/ministryofjustice/hmpps-manage-supervisions")
        CloudPlatform.kubernetes.add(this)
      }
    }

    override fun defineRelationships() {
      ProbationPractitioners.nps.uses(manageASupervisionUi, "manages supervisions with")

      manageASupervisionUi.uses(HMPPSAuth.system, "authenticates via")
      manageASupervisionUi.uses(Delius.communityApi, "Gets SU and past-offence details from")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system,
      "manage-a-supervision-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "manage-a-supervision-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createDeploymentView(system, "manage-a-supervision-deployment", "The Production deployment scenario for the Manage A Supervision service").apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
