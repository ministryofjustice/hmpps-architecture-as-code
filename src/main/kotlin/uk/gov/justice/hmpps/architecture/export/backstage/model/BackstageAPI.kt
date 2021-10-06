package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageModelType.API

enum class BackstageAPISpecType {
  @JsonProperty("openapi") OPEN_API,
}

data class BackstageAPISpecDefinition(
  @JsonProperty("\$text") val text: String
)

data class BackstageAPISpec(
  val type: BackstageAPISpecType,
  val lifecycle: BackstageLifecycle,
  val system: String,
  val owner: String = "hmpps-undefined",
  val definition: BackstageAPISpecDefinition,
)

data class BackstageAPI(
  override val metadata: BackstageMetadata,
  val spec: BackstageAPISpec,
) : BackstageModel(kind = API, metadata = metadata)


