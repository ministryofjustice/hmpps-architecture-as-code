package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet
import uk.gov.justice.hmpps.architecture.annotations.APIDocs
import uk.gov.justice.hmpps.architecture.annotations.Tags

class DigitalPrisonsNetwork private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var auth: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("Digital Prisons Network", "Provides in-cell devices and access to digital services to prisoners")

      auth = system.addContainer(
        "Azure AD",
        "Azure Active Directory, supporting OAuth2 applications and network/device login",
        "Active Directory"
      )
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
