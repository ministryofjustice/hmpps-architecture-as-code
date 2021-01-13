package uk.gov.justice.hmpps.architecture

import com.structurizr.model.Element
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import java.io.File

fun cloneRepository(repoUrl: String, e: Element): File {
  val cloneDir = App.EXPORT_LOCATION.resolve("clones").resolve(e.id)

  println("[git] Processing $repoUrl")
  if (cloneDir.resolve(".git").exists()) {
    update(cloneDir)
  } else {
    clone(repoUrl, cloneDir)
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
