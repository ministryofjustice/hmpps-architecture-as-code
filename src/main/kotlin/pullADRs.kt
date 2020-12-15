package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.AdrToolsImporter
import com.structurizr.model.SoftwareSystem
import org.eclipse.jgit.api.Git
import uk.gov.justice.hmpps.architecture.annotations.ADRSource
import java.io.File

fun pullADRs(workspace: Workspace) {
  val systemsWithADRs = workspace.model.softwareSystems.stream().filter { ADRSource.getFrom(it) != null }

  systemsWithADRs.forEach {
    println()
    println("[pullADRs] For ${it.name}")

    val cloneDir = cloneADRRepo(it)
    val decisions = AdrToolsImporter(workspace, cloneDir.resolve("doc/adr"))
      .importArchitectureDecisionRecords(it)

    println("[pullADRs] Imported ${decisions.size} decisions")
  }
}

private fun cloneADRRepo(system: SoftwareSystem): File {
  val repo = ADRSource.getFrom(system)

  val cloneDir = App.EXPORT_LOCATION.resolve("clones").resolve(system.id)
  cloneDir.deleteRecursively()
  cloneDir.mkdirs()

  println("[pullADRs] Cloning ADR repo ($repo) to directory '$cloneDir'")
  Git.cloneRepository()
    .setURI(repo)
    .setDirectory(cloneDir)
    .call()

  return cloneDir
}
