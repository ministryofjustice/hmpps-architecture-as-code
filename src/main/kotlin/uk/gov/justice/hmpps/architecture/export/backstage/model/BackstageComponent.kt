package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageModelType.COMPONENT

enum class BackstageComponentSpecType {
  @JsonProperty("service") SERVICE,
  @JsonProperty("database") DATABASE,
  @JsonProperty("queue") QUEUE,
  @JsonProperty("topic") TOPIC,
  @JsonProperty("frontend") FRONTEND,
}

@JsonInclude(NON_EMPTY)
data class BackstageComponentSpec(
  val type: BackstageComponentSpecType,
  val lifecycle: BackstageLifecycle,
  val system: String,
  val owner: String = "hmpps-undefined",
  val dependsOn: List<String> = emptyList(),
  val providesApis: List<String> = emptyList(),
  val consumesApis: List<String> = emptyList(),
)

data class BackstageComponent(
  override val metadata: BackstageMetadata,
  val spec: BackstageComponentSpec,

  @JsonIgnore
  val api: BackstageAPI? = null,
) : BackstageModel(kind = COMPONENT, metadata = metadata)
