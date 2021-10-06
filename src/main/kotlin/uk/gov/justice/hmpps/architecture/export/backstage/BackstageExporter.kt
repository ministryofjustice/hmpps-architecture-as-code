package uk.gov.justice.hmpps.architecture.export.backstage

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import uk.gov.justice.hmpps.architecture.annotations.Tags
import uk.gov.justice.hmpps.architecture.export.backstage.apispec.OpenApiSpecFinder
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageAPI
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageAPISpec
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageAPISpecDefinition
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageAPISpecType.OPEN_API
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponent
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpec
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpecType
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpecType.DATABASE
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpecType.FRONTEND
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpecType.QUEUE
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpecType.SERVICE
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageComponentSpecType.TOPIC
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageLifecycle
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageLifecycle.DEPRECATED
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageLifecycle.PRODUCTION
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageMetadata
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageMetadataLink
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageMetadataLinkIcon.GITHUB
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageSystem
import java.util.Locale

class BackstageExporter(private val openApiSpecFinder: OpenApiSpecFinder = OpenApiSpecFinder()) {

  private val objectMapper: ObjectMapper = ObjectMapper(YAMLFactory())

  init {
    objectMapper.registerModule(KotlinModule.Builder().build())
  }

  fun export(workspace: Workspace): String {
    val allSystems = workspace.model.softwareSystems.map { it.convert() }
    val allComponents = allSystems.flatMap { it.components }
    val allApis = allSystems.flatMap { it.components.mapNotNull { component -> component.api } }
    val allModels = allSystems
      .plus(allComponents)
      .plus(allApis)

    return allModels
      .sortedBy { it.metadata.name }
      .map { objectMapper.writeValueAsString(it) }
      .joinToString("")
  }

  private fun SoftwareSystem.convert(): BackstageSystem {
    return BackstageSystem(
      metadata = BackstageMetadata(
        name = backstageId(),
        title = name,
        description = description,
      ),
      components = containers.map { it.convert() }
    )
  }

  private fun Container.githubLink(): BackstageMetadataLink? {
    return url?.let {
      BackstageMetadataLink(
        url = it,
        title = "GitHub Repo",
        icon = GITHUB,
      )
    }
  }

  private fun Container.apiDocsLink(): BackstageMetadataLink? {
    return this.properties[API_DOCS_URL]?.let {
      BackstageMetadataLink(
        url = it,
        title = "API Docs",
      )
    }
  }

  private fun Container.lifecycle(): BackstageLifecycle {
    return if (hasTag(Tags.DEPRECATED.name)) DEPRECATED else PRODUCTION
  }

  private fun Container.type(): BackstageComponentSpecType {
    return when {
      hasTag(Tags.DATABASE.name) -> DATABASE
      hasTag(Tags.QUEUE.name) -> QUEUE
      hasTag(Tags.TOPIC.name) -> TOPIC
      hasTag(Tags.WEB_BROWSER.name) -> FRONTEND
      else -> SERVICE
    }
  }

  private fun Container.api(): BackstageAPI? {
    return properties[API_DOCS_URL]
      ?.let { openApiSpecFinder.deriveApiSpecFor(it) }
      ?.let {
        BackstageAPI(
          metadata = BackstageMetadata(
            name = backstageId(),
            description = "API provided by ${backstageId()}",
          ),
          spec = BackstageAPISpec(
            type = OPEN_API,
            lifecycle = lifecycle(),
            system = softwareSystem.backstageId(),
            definition = BackstageAPISpecDefinition(text = it),
          )
        )
      }
  }

  private fun Container.convert(): BackstageComponent {
    val api = api()
    val relationships = relationships
      .map { it.destination }
      .filterIsInstance<Container>()

    return BackstageComponent(
      metadata = BackstageMetadata(
        name = backstageId(),
        title = name,
        description = description,
        links = listOfNotNull(githubLink(), apiDocsLink()),
        annotations = url?.let { mapOf("backstage.io/source-location" to "url:$url") }
      ),
      spec = BackstageComponentSpec(
        type = type(),
        lifecycle = lifecycle(),
        system = softwareSystem.backstageId(),
        providesApis = listOfNotNull(api?.metadata?.name),
        consumesApis = relationships.map { it.backstageId() },
        dependsOn = relationships.map { "Component:${it.backstageId()}" },
      ),
      api = api,
    )
  }

  private fun SoftwareSystem.backstageId(): String {
    return backstageId(name)
  }

  private fun Container.backstageId(): String {
    val idWithContext = backstageId("${softwareSystem.name}-$name")
    val genericContainerName = "^(database|backend|frontend)$".toRegex()

    if (genericContainerName.matches(name.lowercase(Locale.getDefault()))) return idWithContext

    if ("api".equals(name, ignoreCase = true)) {
      return if (softwareSystem.name.endsWith("api", ignoreCase = true)) softwareSystem.name else idWithContext
    }

    return backstageId(name)
  }

  private fun backstageId(name: String): String {
    return name.lowercase().replace(Regex("""[^a-z0-9]+"""), "-")
  }

  private companion object {
    const val API_DOCS_URL = "api-docs-url"
  }
}
