package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.AutomaticDocumentationTemplate
import com.structurizr.model.Container
import com.structurizr.model.Model
import org.eclipse.jgit.api.errors.GitAPIException
import uk.gov.justice.hmpps.architecture.annotations.APIDocs
import java.io.File
import java.io.IOException
import java.io.PrintWriter

fun defineDocumentation(workspace: Workspace) {
  val docsRoot = File(App.EXPORT_LOCATION, "docs/")
  docsRoot.mkdirs()

  val w = File(docsRoot, "apidocs.md").printWriter()
  writeAPIs(w, workspace.model)
  w.close()

  val template = AutomaticDocumentationTemplate(workspace)
  template.addSections(docsRoot)
}

private fun writeAPIs(w: PrintWriter, model: Model) {
  w.println("## Published APIs")
  w.println("")

  w.println("| Software System | API name | Purpose | Links |")
  w.println("| --- | --- | --- | --- |")

  containersWithAPIDocs(model)
    .also { println("[defineDocumentation] Found ${it.size} APIs with documentation URLs") }
    .forEach { writeAPI(w, it) }
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
private fun containersWithAPIDocs(model: Model): List<Container> {
  val containers = model.softwareSystems
    .sortedBy { it.name.toLowerCase() }
    .flatMap { it.containers }

  containers
    .filter { it.url.orEmpty().contains("github.com/ministryofjustice") }
    .forEach {
      val oldUrl = APIDocs.getFrom(it)
      val url = readAPIDocsURLFromRepoReadmeBadge(it)

      if (url != null) {
        if (oldUrl != null) {
          println("[defineDocumentation] overriding API docs URL\n - from ${oldUrl}\n - to   $url")
        }
        APIDocs(url).addTo(it)
      }
    }

  return containers.filter { APIDocs.getFrom(it) != null }
}

val BADGE_URL_PATTERN = Regex("""\[!\[API docs]\(.*\)]\((.*)\)""")
private fun readAPIDocsURLFromRepoReadmeBadge(app: Container): String? {
  var readmeContents = ""
  try {
    println()
    val repoDir = cloneRepository(app.url, app)
    readmeContents = repoDir.resolve("README.md").readText()
  } catch (e: GitAPIException) {
    println("[defineDocumentation] ignoring: $e")
  } catch (e: IOException) {
    println("[defineDocumentation] ignoring: $e")
  }

  val m = BADGE_URL_PATTERN.find(readmeContents)
  return m?.groups?.get(1)?.value
}
