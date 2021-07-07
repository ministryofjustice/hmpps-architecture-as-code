package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.*
import uk.gov.justice.hmpps.architecture.annotations.Tags

class ManageSupervisions private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var manageSupervisionsUi: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Manage Supervisions",
        "Digital Service for A, "
          + "B, " +
          "and C"
      )

      manageSupervisionsUi = system.addContainer(
        "Manage Supervisions UI",
        "Web application, displaying service user history and handling appointment booking",
        "Typescript, Node, NestJS, Express"
      ).apply {
        setUrl("https://github.com/ministryofjustice/hmpps-manage-supervisions")
      }
    }

    override fun defineRelationships() {
      ProbationPractitioners.nps.uses(manageSupervisionsUi, "manages supervisions with")

      manageSupervisionsUi.uses(HMPPSAuth.system, "authenticates via")
      manageSupervisionsUi.uses(Delius.communityApi, "Gets SU and past-offence details from")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system,
      "manage-supervisions-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "manage-supervisions-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
