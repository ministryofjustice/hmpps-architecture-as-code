package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.Model
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import org.eclipse.jgit.api.errors.GitAPIException
import java.io.File

fun containersWithGit(model: Model): List<Container> {
  return model.softwareSystems
    .flatMap { it.containers }
    .sortedBy { it.softwareSystem.name.lowercase() + it.name.lowercase() }
    .filter { it.url.orEmpty().startsWith("https://github.com/ministryofjustice") }
}

fun cloneRepository(e: Element): File? {
  return cloneRepository(e.url, e)
}

fun cloneRepository(repoUrl: String, e: Element): File? {
  val cloneDir = App.EXPORT_LOCATION.resolve("clones").resolve(File(repoUrl).name)

  try {
    if (cloneDir.resolve(".git").exists()) {
      println("[git] $repoUrl already cloned -- doing nothing (force new clone with rm -rf exports/clones/)")
    } else {
      println("[git] Cloning $repoUrl")
      clone(repoUrl, cloneDir)
    }
  } catch (e: GitAPIException) {
    println("[git] ignoring: $e")
    return null
  }

  return cloneDir
}

private fun clone(repoUrl: String, cloneDir: File) {
  Git.cloneRepository()
    .setURI(repoUrl)
    .setDirectory(cloneDir)
    .call()
}

private fun update(cloneDir: File) {
  Git.open(cloneDir)
    .pull()
    .setFastForward(MergeCommand.FastForwardMode.FF_ONLY)
    .call()
}
