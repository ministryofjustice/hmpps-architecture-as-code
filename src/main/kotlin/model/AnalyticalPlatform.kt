package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.Tags

class AnalyticalPlatform private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var landingBucket: Container

    override fun defineModelEntities(model: Model) {
      val platform = model.addSoftwareSystem("Analytical Platform")
      landingBucket = platform.addContainer(
        "Analytical Platform landing bucket",
        "Storage area where data ingestion for analytics and data science starts",
        "S3"
      ).apply {
        Tags.DATABASE.addTo(this)
      }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
