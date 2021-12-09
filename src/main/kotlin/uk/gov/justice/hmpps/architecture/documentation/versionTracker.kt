package uk.gov.justice.hmpps.architecture.documentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.structurizr.model.Container
import uk.gov.justice.hmpps.architecture.cloneRepository
import java.io.File

data class Version(
  val softwareSystem: String,
  val application: String,
  val applicationUrl: String,
  val circleciOrbVersions: List<String>,
  val gradleBootPluginVersion: String,
  val chartVersions: List<String>,
)

fun parseVersions(containersWithGit: List<Container>): List<Version> {
  return containersWithGit
    .also { println("[parseVersions] Found ${it.size} applications with github repos") }
    .mapNotNull { parseVersion(it) }
}

private fun parseVersion(container: Container): Version? {
  val r = cloneRepository(container) ?: return null
  return Version(
    softwareSystem = container.softwareSystem.name,
    application = container.name,
    applicationUrl = container.url,
    circleciOrbVersions = readCircleOrbVersion(r),
    gradleBootPluginVersion = readGradlePluginVersion(r),
    chartVersions = readHelmChartDependencies(r)
  )
}

private fun readCircleOrbVersion(repo: File): List<String> {
  val circleConfig = repo.resolve(".circleci").resolve("config.yml").takeIf { it.exists() }?.readText().orEmpty()

  val circleOrb = Regex("ministryofjustice/(hmpps@[\\d.]*)").find(circleConfig)?.groups?.get(1)?.value
  val dpsOrb = Regex("ministryofjustice/(dps@[\\d.]*)").find(circleConfig)?.groups?.get(1)?.value
  return listOfNotNull(circleOrb, dpsOrb)
}

const val GRADLE_PATTERN = """id\("uk.gov.justice.hmpps.gradle-spring-boot"\) version "([^"]*)""""
private fun readGradlePluginVersion(repo: File): String {
  val buildFileKt = repo.resolve("build.gradle.kts").takeIf { it.exists() }?.readText().orEmpty()
  val buildFile = repo.resolve("build.gradle").takeIf { it.exists() }?.readText().orEmpty()

  return listOfNotNull(
    Regex(GRADLE_PATTERN).find(buildFileKt)?.groups?.get(1)?.value,
    Regex(GRADLE_PATTERN).find(buildFile)?.groups?.get(1)?.value,
  ).firstOrNull() ?: ""
}

private fun readHelmChartDependencies(repo: File): List<String> {
  val chartFile = repo.resolve("helm_deploy")
    .walkTopDown().find { it.name == "Chart.yaml" }
    ?: return listOf("no helm chart")

  val dependencies = ObjectMapper(YAMLFactory()).readTree(chartFile).get("dependencies")
    ?: return listOf("standalone chart")

  return dependencies.map { "${it.get("name").textValue()}@${it.get("version").textValue()}" }
}
