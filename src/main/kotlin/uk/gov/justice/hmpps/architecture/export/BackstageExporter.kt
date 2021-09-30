package uk.gov.justice.hmpps.architecture.export

import com.structurizr.Workspace
import com.structurizr.io.AbstractExporter
import com.structurizr.io.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags

class BackstageExporter : AbstractExporter() {

  fun export(workspace: Workspace): String {
    val writer = IndentingWriter()
    workspace.model.softwareSystems.forEach { writer.writeSystem(it) }
    return writer.toString()
  }

  private fun IndentingWriter.writeSystem(softwareSystem: SoftwareSystem) {
    writeSystemHeader()
    writeMetadata(softwareSystem)
    writeSpec(softwareSystem)

    softwareSystem.containers.forEach { writeComponent(it) }
  }

  private fun IndentingWriter.writeComponent(container: Container) {
    writeComponentHeader()
    writeMetadata(container)
    writeSpec(container)

    val relationships = container.relationships.map { it.destination }.filterIsInstance<Container>()

    when {
      relationships.isNotEmpty() -> {
        indent()
        writeLine("dependsOn:")
        relationships.forEach {
          indent()
          writeLine("- Component:${it.backstageId()}")
          outdent()
        }
        outdent()
      }
    }
  }

  private fun IndentingWriter.writeSystemHeader() {
    writeLine("---")
    writeLine("apiVersion: backstage.io/v1alpha1")
    writeLine("kind: System")
  }

  private fun IndentingWriter.writeComponentHeader() {
    writeLine("---")
    writeLine("apiVersion: backstage.io/v1alpha1")
    writeLine("kind: Component")
  }

  private fun IndentingWriter.writeMetadata(softwareSystem: SoftwareSystem) {
    writeLine("metadata:")
    indent()

    writeLine("name: ${softwareSystem.backstageId()}")
    writeLine("title: \"${softwareSystem.name}\"")
    writeLine("description: \"${softwareSystem.description}\"")

    outdent()
  }

  private fun IndentingWriter.writeMetadata(container: Container) {
    writeLine("metadata:")
    indent()

    writeLine("name: ${container.backstageId()}")
    writeLine("title: \"${container.name}\"")
    writeLine("description: \"${container.description}\"")

    outdent()
  }

  private fun IndentingWriter.writeSpec(softwareSystem: SoftwareSystem) {
    writeLine("spec:")
    indent()

    writeLine("owner: hmpps-undefined")

    outdent()
  }

  private fun IndentingWriter.writeSpec(container: Container) {

    val lifecycle = if (container.hasTag(Tags.DEPRECATED.name)) "deprecated" else "production"

    writeLine("spec:")
    indent()

    writeLine("type: service")
    writeLine("lifecycle: $lifecycle")
    writeLine("owner: hmpps-undefined")

    outdent()
  }

  private fun SoftwareSystem.backstageId(): String {
    return backstageId(this.name)
  }

  private fun Container.backstageId(): String {
    return backstageId("s" + this.softwareSystem.id + "_" + this.name)
  }

  private fun backstageId(name: String): String {
    return name.lowercase().replace(Regex("""[^a-z0-9]+"""), "-")
  }
}
