package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Container
import com.structurizr.model.Person

interface delivers {
  fun delivers(
    from: Container,
    to: List<Triple<List<Person>, String, String>>
  )
}
