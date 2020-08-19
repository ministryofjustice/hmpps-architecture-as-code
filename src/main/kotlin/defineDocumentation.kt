package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.documentation.AutomaticDocumentationTemplate
import com.structurizr.model.Container
import com.structurizr.model.Model
import java.io.File
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

  val apis = model.softwareSystems
    .sortedBy { it.name.toLowerCase() }
    .flatMap { it.containers }
    .filter { it.properties.get("api-docs-url") != null }
  w.println("| Software System | API name | Purpose | Links |")
  w.println("| --- | --- | --- | --- |")
  apis.forEach { writeAPI(w, it) }
}

private fun writeAPI(w: PrintWriter, apiContainer: Container) {
  w.print("| ")
  w.print(apiContainer.softwareSystem.name)

  w.print("| ")
  w.print(apiContainer.name)

  w.print("| ")
  w.print(apiContainer.description)

  w.print("| ")
  val githubLink = apiContainer.url
  val apidocsLink = apiContainer.properties.get("api-docs-url")
  w.print(" [GitHub](%s)".format(githubLink))
  w.print(" [APIdocs](%s)".format(apidocsLink))

  w.println("|")
}
