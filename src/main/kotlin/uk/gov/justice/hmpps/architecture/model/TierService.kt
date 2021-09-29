package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class TierService private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var tierDb: Container
    lateinit var tierService: Container
    lateinit var tierToDeliusUpdate: Container
    lateinit var tierSqsTool: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Tier Service",
        "Calculates and stores unified tier scores, representing the risk and needs of people under supervision"
      )

      tierDb = system.addContainer(
        "Tier Database",
        "Holds tier",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      tierService = system.addContainer(
        "Tier Service",
        "Tier micro-service containing business logic, persistence logic and REST API",
        "Kotlin + Spring Boot"
      ).apply {
        CloudPlatform.kubernetes.add(this)
        uses(tierDb, "connects to", "JDBC")
        setUrl("https://github.com/ministryofjustice/hmpps-tier")
      }

      tierToDeliusUpdate = system.addContainer(
        "Tier to Delius Update",
        "Synchronises tier back into nDelius",
        "Kotlin + Spring Boot"
      ).apply {
        CloudPlatform.kubernetes.add(this)
        setUrl("https://github.com/ministryofjustice/hmpps-tier-to-delius-update")
      }

      tierSqsTool = system.addContainer(
        "Tier SQS Tool",
        "Tool to manage SQS messaging infrastructure such as automated retries",
        "Kotlin + Spring Boot"
      ).apply {
        CloudPlatform.kubernetes.add(this)
        setUrl("https://github.com/ministryofjustice/hmpps-tier-sqs-tool")
      }
    }

    override fun defineRelationships() {
      tierService.uses(Delius.probationOffenderEvents, "to know when to recalculate a tier, consumes management tier updated events from", "SNS+SQS")
      tierService.uses(Delius.communityApi, "retrieves data required for tier calculation from", "REST")
      tierService.uses(OASys.assessmentsApi, "retrieves data required for tier calculation from", "REST")
      tierService.uses(HMPPSDomainEvents.topic, "publishes tier update events to", "SNS")

      tierToDeliusUpdate.uses(HMPPSDomainEvents.topic, "consumes tier updates events from", "SNS+SQS")
      tierToDeliusUpdate.uses(Delius.communityApi, "updates tier in Delius with", "REST")
    }

    override fun defineViews(views: ViewSet) {

      views.createSystemContextView(system, "tier-service-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "tier-service-container", null).apply {
        addDefaultElements()
        addAllContainersAndInfluencers()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
