package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.ProblemArea
import uk.gov.justice.hmpps.architecture.annotations.Tags

class Interventions private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var publishUI: Container
    lateinit var publish: Container
    lateinit var deliverUI: Container
    lateinit var deliver: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Getting the right rehabilitation",
        "Supports maintaning interventions and services and finding, booking, delivering and monitoring interventions (currently only Dynamic Framework)"
      ).apply {
        ProblemArea.GETTING_THE_RIGHT_REHABILITATION.addTo(this)
        Tags.PLANNED.addTo(this)
      }

      publish = system.addContainer(
        "Publish an intervention service",
        "Responsible for curating published interventions and services",
        "Java or Kotlin"
      )

      deliver = system.addContainer(
        "Deliver an intervention service",
        "Responsible for tracking referrals, appointments and the delivery for interventions and services",
        "Java or Kotlin"
      )

      publishUI = system.addContainer("Publish an intervention UI", "Responsible for curating published interventions and services", "node").apply {
        uses(publish, "adds, quality checks and revokes interventions with")
        Tags.WEB_BROWSER.addTo(this)
      }

      deliverUI = system.addContainer("Find, book, record, monitor an intervention UI", "Responsible for intervention delivery", "node").apply {
        uses(publish, "finds interventions with")
        uses(deliver, "books, schedules, delivers and monitors interventions with")
        Tags.WEB_BROWSER.addTo(this)
      }

      system.containers.forEach { Tags.PLANNED.addTo(it) }
    }

    override fun defineRelationships() {
      publish.uses(HMPPSAuth.app, "authenticates, authorises users and requests access tokens from", "OAuth2/JWT")
      deliver.uses(HMPPSAuth.app, "authenticates, authorises users and requests access tokens from", "OAuth2/JWT")

      deliver.uses(OASys.assessmentsApi, "retrieves service user risks and needs via", "REST/HTTP")
      deliver.uses(Delius.offenderSearch, "retrieves service user information via", "REST/HTTP")

      InterventionTeams.dynamicFrameworkProvider.uses(publishUI, "maintains directory of dynamic framework interventions and services in")
      InterventionTeams.dynamicFrameworkProvider.uses(deliverUI, "tracks delivery of dynamic framework interventions and services in")
      ProbationPractitioners.nps.uses(deliverUI, "refers and monitors progress of their service users' interventions and services")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "interventions-context", "Context overview of the digital intervention services").apply {
        addDefaultElements()
        removeRelationshipsNotConnectedToElement(system)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "interventions-container", "Container overview of the digital intervention services").apply {
        addDefaultElements()
        system.containers.forEach(::addNearestNeighbours)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
