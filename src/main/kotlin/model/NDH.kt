package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class NDH private constructor() {
  companion object : HMPPSSoftwareSystem {
    lateinit var system: SoftwareSystem
    private lateinit var initialSearch: Container
    private lateinit var offenderDetails: Container
    private lateinit var offenderAssessments: Container
    private lateinit var offenderRiskUpdates: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "NDH",
        "(NOMIS Data Hub) Responsible for pulling/pushing data between HMPPS case management systems"
      )

      initialSearch = system.addContainer(
        "NDH offender initial search",
        "Used to identify if there are existing records in Delius for an offender",
        "endpoint"
      )

      offenderDetails = system.addContainer(
        "NDH offender details",
        "Retrieves data for a specific offender from either Delius or NOMIS and transforms it",
        "endpoint"
      )

      offenderAssessments = system.addContainer(
        "NDH offender assessments",
        "Sends risk assessment data from OASys to Delius",
        "endpoint"
      )

      offenderRiskUpdates = system.addContainer(
        "NDH offender risk updates",
        "Sends specific risk data from OAsys to Delius",
        "endpoint"
      )
    }

    override fun defineRelationships() {
      defineImpliedRelationships()
      defineDirectRelationships()
    }

    override fun defineViews(views: ViewSet) {
    }

    fun defineImpliedRelationships() {
      // these are the flows what NDH enables
      // it's very useful to see these relationships between NOMIS, OASys and Delius
      // without having to track it through a 'hub'
      NOMIS.system.uses(OASys.system, "offender details are copied into", "NDH")
      OASys.system.uses(NOMIS.system, "offender details are looked up from", "NDH")
      OASys.system.uses(Delius.system, "offender risk assessment and specific risk data are copied into", "NDH")
      OASys.system.uses(Delius.system, "offender details are looked up from", "NDH")
    }

    fun defineDirectRelationships() {
      system.uses(NOMIS.system, "periodically polls the NOMIS Events table to look for changes to offender data via", "Oracle XTAG queue")
      system.uses(OASys.system, "sends changed offender data collected from NOMIS via", "SOAP/XML")

      OASys.system.uses(initialSearch, "used during assessment process to identify if there are existing records in Delius for an offender via")
      initialSearch.uses(Delius.system, "searches for offender existence in")

      OASys.system.uses(offenderDetails, "used during assessment process to retrieve offender information from NOMIS or Delius via")
      offenderDetails.uses(Delius.system, "retrieves offender details from")
      offenderDetails.uses(NOMIS.system, "retrieves offender details from")

      OASys.system.uses(offenderAssessments, "used during certain points in the assessment process to receive risk assessment data from")
      offenderAssessments.uses(Delius.system, "sends risk assessment data to", "SOAP/XML/ActiveMQ")

      OASys.system.uses(offenderRiskUpdates, "used during certain points in the assessment process to receive specific risk data from")
      offenderRiskUpdates.uses(Delius.system, "sends specific risk data to", "SOAP/XML/ActiveMQ")
    }
  }
}
