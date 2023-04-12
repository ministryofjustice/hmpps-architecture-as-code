package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.structurizr.model.Container
import uk.gov.justice.hmpps.architecture.annotations.APIDocs
import uk.gov.justice.hmpps.architecture.documentation.BranchMetric
import uk.gov.justice.hmpps.architecture.documentation.Version
import uk.gov.justice.hmpps.architecture.documentation.parseBranchMetrics
import uk.gov.justice.hmpps.architecture.documentation.parseVersions
import java.io.File
import java.io.IOException
import java.io.PrintWriter

fun defineDocumentation(workspace: Workspace) {
  val docsRoot = File(App.EXPORT_LOCATION, "docs/")
  docsRoot.mkdirs()

  val containersWithGitRepos = containersWithGit(workspace.model)

  val docs = File(docsRoot, "01-apidocs.md")
  docs.printWriter().let {
    writeAPIs(it, containersWithGitRepos)
    it.close()
  }

  val dependencies = File(docsRoot, "02-dependencies.md")
  dependencies.printWriter().let {
    writeDependencies(it, containersWithGitRepos)
    it.close()
  }

  val branches = File(docsRoot, "03-branches.md")
  branches.printWriter().let {
    writeBranchMetrics(it, containersWithGitRepos)
    it.close()
  }

  val template = workspace.documentation
  template.addSection(Section(Format.Markdown, docs.bufferedReader().use { it.readText() }))
  template.addSection(Section(Format.Markdown, dependencies.bufferedReader().use { it.readText() }))
  template.addSection(Section(Format.Markdown, branches.bufferedReader().use { it.readText() }))
}

private fun writeAPIs(w: PrintWriter, containersWithGit: List<Container>) {
  w.println("## Published APIs")
  w.println("")

  w.println("| Software System | API name | Purpose | Links |")
  w.println("| --- | --- | --- | --- |")

  containersWithAPIDocs(containersWithGit)
    .also { println("[defineDocumentation] Found ${it.size} APIs with documentation URLs") }
    .forEach { writeAPI(w, it) }
}

private fun writeDependencies(w: PrintWriter, containersWithGit: List<Container>) {
  w.println("## Dependencies")
  w.println("")

  w.println("- Latest [CircleCI orb](https://circleci.com/developer/orbs/orb/ministryofjustice/hmpps)")
  w.println("- Latest [gradle-spring-boot](https://plugins.gradle.org/plugin/uk.gov.justice.hmpps.gradle-spring-boot)")
  w.println("- Latest [helm charts](https://github.com/ministryofjustice/hmpps-helm-charts/releases)")
  w.println("")

  w.println("| Software System | Application | CircleCI orb versions | gradle-spring-boot version | helm charts")
  w.println("| --- | --- | --- | --- | --- |")

  parseVersions(containersWithGit)
    .forEach { writeDependency(w, it) }
}

fun writeDependency(w: PrintWriter, v: Version) {
  w.print("| ")
  w.print(v.softwareSystem)

  w.print("| ")
  w.print("[${v.application}](${v.applicationUrl})")

  w.print("| ")
  w.print(v.circleciOrbVersions.joinToString(", "))

  w.print("| ")
  w.print(v.gradleBootPluginVersion)

  w.print("| ")
  w.print(v.chartVersions.joinToString(", "))

  w.println("|")
}

private fun writeBranchMetrics(w: PrintWriter, containersWithGit: List<Container>) {
  w.println("## Branches")
  w.println("")

  w.println("| Software System | Application | Unmerged branches | Oldest unmerged branch | Oldest branch date")
  w.println("| --- | --- | --- | --- | --- |")

  parseBranchMetrics(containersWithGit)
    .forEach { writeBranchMetric(w, it) }
}

fun writeBranchMetric(w: PrintWriter, m: BranchMetric) {
  w.print("| ")
  w.print(m.softwareSystem)

  w.print("| ")
  w.print("[${m.application}](${m.applicationUrl})")

  w.print("| ")
  w.print(m.unmergedBranches)

  w.print("| ")
  w.print(m.oldestUnmergedBranch?.name ?: "")

  w.print("| ")
  w.print(m.oldestUnmergedBranch?.latestCommitTime?.toLocalDate() ?: "")

  w.println("|")
}

@Suppress("DEPRECATION")
private fun writeAPI(w: PrintWriter, apiContainer: Container) {
  w.print("| ")
  w.print(apiContainer.softwareSystem.name)

  w.print("| ")
  w.print(apiContainer.name)

  w.print("| ")
  w.print(apiContainer.description)

  w.print("| ")
  val githubLink = apiContainer.url
  val apidocsLink = APIDocs.getFrom(apiContainer)
  w.print(" [GitHub](%s)".format(githubLink))
  w.print(" [APIdocs](%s)".format(apidocsLink))

  w.println("|")
}

@Suppress("DEPRECATION")
private fun containersWithAPIDocs(containersWithGit: List<Container>): List<Container> {
  return containersWithGit
    .map { pullApiDocs(it) }
    .filter { APIDocs.getFrom(it) != null }
}

@Suppress("DEPRECATION")
private fun pullApiDocs(container: Container): Container {
  val oldUrl = APIDocs.getFrom(container)
  val url = readAPIDocsURLFromRepoReadmeBadge(container)

  if (url != null) {
    if (oldUrl != null) {
      println("[defineDocumentation] overriding API docs URL\n - from ${oldUrl}\n - to   $url")
    }
    APIDocs(url).addTo(container)
  }
  return container
}

val BADGE_URL_PATTERN = Regex("""\[!\[API docs]\(.*\)]\((.*)\)""")
private fun readAPIDocsURLFromRepoReadmeBadge(app: Container): String? {
  var readmeContents = ""
  try {
    println()
    val repoDir = cloneRepository(app)
    val readme = repoDir?.listFiles { _, name -> name.equals("README.md", true) }?.first()
    readmeContents = readme?.readText().orEmpty()
  } catch (e: IOException) {
    println("[defineDocumentation] ignoring: $e")
  } catch (e: java.util.NoSuchElementException) {
    println("[defineDocumentation] ignoring: $e")
  }

  val m = BADGE_URL_PATTERN.find(readmeContents)
  return m?.groups?.get(1)?.value
}
