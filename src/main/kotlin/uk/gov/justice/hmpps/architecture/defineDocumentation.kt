package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.AutomaticDocumentationTemplate
import com.structurizr.model.Container
import uk.gov.justice.hmpps.architecture.annotations.APIDocs
import uk.gov.justice.hmpps.architecture.documentation.Version
import uk.gov.justice.hmpps.architecture.documentation.parseVersions
import java.io.File
import java.io.IOException
import java.io.PrintWriter

fun defineDocumentation(workspace: Workspace) {
  val docsRoot = File(App.EXPORT_LOCATION, "docs/")
  docsRoot.mkdirs()

  val containersWithGitRepos = containersWithGit(workspace.model)

  File(docsRoot, "apidocs.md").printWriter().let {
    writeAPIs(it, containersWithGitRepos)
    it.close()
  }

  File(docsRoot, "dependencies.md").printWriter().let {
    writeDependencies(it, containersWithGitRepos)
    it.close()
  }

  val template = AutomaticDocumentationTemplate(workspace)
  template.addSections(docsRoot)
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
  w.print(v.circleciOrbVersion)

  w.print("| ")
  w.print(v.gradleBootPluginVersion)

  w.print("| ")
  w.print(v.chartVersions)

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
