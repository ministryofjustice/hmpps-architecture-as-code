package uk.gov.justice.hmpps.architecture.documentation

import com.structurizr.model.Container
import org.eclipse.jgit.api.Git
import uk.gov.justice.hmpps.architecture.cloneRepository
import java.io.File
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

data class Branch(
  val name: String,
  val latestCommitTime: OffsetDateTime,
)

data class BranchMetric(
  val softwareSystem: String,
  val application: String,
  val applicationUrl: String,

  val unmergedBranches: Int,
  val oldestUnmergedBranch: Branch?,
)

fun parseBranchMetrics(containersWithGit: List<Container>): List<BranchMetric> {
  return containersWithGit
    .also { println("[parseBranchMetrics] Found ${it.size} applications with github repos") }
    .mapNotNull { parseBranchMetric(it) }
}

private fun parseBranchMetric(container: Container): BranchMetric? {
  val r = cloneRepository(container) ?: return null

  val branches = remoteBranches(r).map {
    val repo = Git.open(r).repository
    val ref = repo.resolve(it)
    val epochSeconds = repo.parseCommit(ref).commitTime.toLong()
    Branch(
      name = repo.shortenRemoteBranchName(it),
      latestCommitTime = OffsetDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.of("Europe/London")),
    )
  }

  val oldestBranch = branches.minByOrNull { it.latestCommitTime }

  return BranchMetric(
    softwareSystem = container.softwareSystem.name,
    application = container.name,
    applicationUrl = container.url,

    unmergedBranches = branches.size,
    oldestUnmergedBranch = oldestBranch,
  )
}

private fun remoteBranches(repoDir: File, mainBranch: String = "main"): List<String> {
  val p = ProcessBuilder(
    "git",
    "branch",
    "--remote",
    "--list",
    "--no-merged=origin/$mainBranch",
    "--format=%(refname)",
    "--sort=committerdate"
  )
    .directory(repoDir)
    .start()
    .also { it.waitFor(1, TimeUnit.SECONDS) }

  if (p.exitValue() != 0) {
    val message = p.errorStream.bufferedReader().readText()
    return if (message.contains("malformed object name origin/main")) {
      remoteBranches(repoDir, "master")
    } else {
      System.err.println("[parseBranchMetric] cannot retrieve branches: $message")
      listOf()
    }
  }

  return p.inputStream.bufferedReader().readLines()
}
