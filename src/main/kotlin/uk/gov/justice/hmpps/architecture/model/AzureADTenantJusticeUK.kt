package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.HMPPSSoftwareSystem

class AzureADTenantJusticeUK private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var directory: Container
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Azure AD JusticeUK Tenant",
        "Azure AD tenant synced with uses from the on-prem Atos AD domain (DOM1)"
      )

      directory = system.addContainer(
        "JusticeUK Directory",
        "Directory service containing DOM1 identities",
        "Azure AD"
      ).apply {
        Azure.root.add(this)
      }
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
