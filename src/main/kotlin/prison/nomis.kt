package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem

class NOMIS(model: Model) {
  val system: SoftwareSystem

  init {
    val nomis = model.addSoftwareSystem("NOMIS", """
    National Offender Management Information System,
    the case management system for offender data in use in custody - both public and private prisons
    """.trimIndent())

    val db = nomis.addContainer("NOMIS database", null, null).apply {
      addTags("database")
    }

    nomis.addContainer("Elite2 API", "API over the NOMIS DB used by Digital Prison team applications and services", null).apply {
      setUrl("https://github.com/ministryofjustice/elite2-api")
      addTags("database")
      uses(db, "JDBC")
    }

    nomis.addContainer("Custody API", "New Tech Nomis: Offender API.  The service provides REST access to the Nomis Oracle DB offender information.", null).apply {
      setUrl("https://github.com/ministryofjustice/custody-api")
      uses(db, "JDBC")
    }

    nomis.addContainer("NOMIS API (Deprecated)", "(Deprecated) REST API for NOMIS which connects to Oracle DB. Deprecated - please use Custody API instead.", null).apply {
      setUrl("https://github.com/ministryofjustice/nomis-api")
      uses(db, "JDBC")
    }

    nomis.addContainer("Offender Events", "Publishes Events about offender change to Pub / Sub Topics.", null).apply {
      setUrl("https://github.com/ministryofjustice/offender-events")
      uses(db, "JDBC")
    }

    system = nomis
  }
}
