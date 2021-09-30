package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.AdrToolsImporter
import uk.gov.justice.hmpps.architecture.annotations.ADRSource

fun pullADRs(workspace: Workspace) {
  workspace.model.softwareSystems.stream()
    .filter { ADRSource.getFrom(it) != null }
    .forEach {
      val cloneDir = cloneRepository(ADRSource.getFrom(it)!!, it)
      val decisions = AdrToolsImporter(workspace, cloneDir?.resolve("doc/adr"))
        .importArchitectureDecisionRecords(it)

      println("[pullADRs] Imported ${decisions.size} decisions for '${it.name}'")
    }
}
