package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Model
import com.structurizr.view.ViewSet

interface HMPPSSoftwareSystem {
  fun defineModelEntities(model: Model)
  fun defineRelationships()
  fun defineViews(views: ViewSet)
}
