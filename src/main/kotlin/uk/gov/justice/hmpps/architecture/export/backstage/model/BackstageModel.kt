package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

enum class BackstageModelType {
  @JsonProperty("System") SYSTEM,
  @JsonProperty("Component") COMPONENT,
  @JsonProperty("API") API,
}

@JsonPropertyOrder("apiVersion", "kind")
abstract class BackstageModel(
  val apiVersion: String = "backstage.io/v1alpha1",
  val kind: BackstageModelType,
  open val metadata: BackstageMetadata,
)
