package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.AutomaticDocumentationTemplate
import com.structurizr.model.Container
import uk.gov.justice.hmpps.architecture.annotations.APIDocs
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
  w.println("")

  w.println("| Software System | Application | CircleCI orb versions | gradle-spring-boot version |")
  w.println("| --- | --- | --- | --- |")

  containersWithGit
    .also { println("[defineDocumentation] Found ${it.size} applications with github repos") }
    .forEach { writeDependency(w, it) }
}

fun writeDependency(w: PrintWriter, app: Container) {
  val r = cloneRepository(app) ?: return

  w.print("| ")
  w.print(app.softwareSystem.name)

  w.print("| ")
  w.print("[${app.name}](${app.url})")

  w.print("| ")
  w.print(readCircleOrbVersion(r))

  w.print("| ")
  w.print(readGradlePluginVersion(r))

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

private fun readCircleOrbVersion(repo: File): String {
  val circleConfig = repo.resolve(".circleci").resolve("config.yml").takeIf { it.exists() }?.readText().orEmpty()

  val circleOrb = Regex("ministryofjustice/(hmpps@[\\d.]*)").find(circleConfig)?.groups?.get(1)?.value
  val dpsOrb = Regex("ministryofjustice/(dps@[\\d.]*)").find(circleConfig)?.groups?.get(1)?.value
  return listOfNotNull(circleOrb, dpsOrb).joinToString("<br>")
}

const val GRADLE_PATTERN = """id\("uk.gov.justice.hmpps.gradle-spring-boot"\) version "([^"]*)""""
private fun readGradlePluginVersion(repo: File): String {
  val buildFileKt = repo.resolve("build.gradle.kts").takeIf { it.exists() }?.readText().orEmpty()
  val buildFile = repo.resolve("build.gradle").takeIf { it.exists() }?.readText().orEmpty()

  return listOfNotNull(
    Regex(GRADLE_PATTERN).find(buildFileKt)?.groups?.get(1)?.value,
    Regex(GRADLE_PATTERN).find(buildFile)?.groups?.get(1)?.value,
  ).joinToString("<br>")
}
