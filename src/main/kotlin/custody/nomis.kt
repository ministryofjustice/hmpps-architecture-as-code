package uk.gov.justice.hmpps.architecture.custody

import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Container
import com.structurizr.model.Model

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
      uses(db, "JDBC")
    }

    system = nomis
  }
}
