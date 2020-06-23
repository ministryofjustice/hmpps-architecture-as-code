package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Model
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem

class NOMIS(model: Model) {
  val system: SoftwareSystem
  val db: Container

  init {
    val nomis = model.addSoftwareSystem("NOMIS", """
    National Offender Management Information System,
    the case management system for offender data in use in custody - both public and private prisons
    """.trimIndent())

    db = nomis.addContainer("NOMIS database", null, "Oracle").apply {
      addTags("database")
    }

    val elite2api = nomis.addContainer("Elite2 API", "API over the NOMIS DB used by Digital Prison team applications and services", "Java").apply {
      setUrl("https://github.com/ministryofjustice/elite2-api")
      addTags("database")
      uses(db, "JDBC")
    }

    nomis.addContainer("Custody API (Deprecated)", "(Deprecated) Offender API.  The service provides REST access to the Nomis Oracle DB offender information. Deprecated - please use " + elite2api.getName() + " instead.", null).apply {
      setUrl("https://github.com/ministryofjustice/custody-api")
      addTags("deprecated")
      uses(db, "JDBC")
    }

    nomis.addContainer("NOMIS API (Deprecated)", "(Deprecated) REST API for NOMIS which connects to Oracle DB. Deprecated - please use " + elite2api.getName() + " instead.", null).apply {
      setUrl("https://github.com/ministryofjustice/nomis-api")
      addTags("deprecated")
      uses(db, "JDBC")
    }

    nomis.addContainer("Offender Events", "Publishes Events about offender change to Pub / Sub Topics.", "Java").apply {
      setUrl("https://github.com/ministryofjustice/offender-events")
      uses(db, "JDBC")
    }

    system = nomis
  }
}
