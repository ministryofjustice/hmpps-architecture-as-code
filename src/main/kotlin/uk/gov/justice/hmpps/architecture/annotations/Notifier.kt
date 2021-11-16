package uk.gov.justice.hmpps.architecture.annotations

import com.structurizr.model.Container
import com.structurizr.model.Person
import uk.gov.justice.hmpps.architecture.Notify

class Notifier {
  companion object : delivers {
    override fun delivers(from: Container, to: List<Triple<List<Person>, String, String>>) {
      from.uses(Notify.system, "delivers notifications via")
      to.map {
        val(people, label, style) = it
        people.map { from.delivers(it, label, style) }
      }
    }
  }
}
