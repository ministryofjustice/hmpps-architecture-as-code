package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageModelType.COMPONENT
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageModelType.SYSTEM

enum class BackstageComponentSpecType {
  @JsonProperty("service") SERVICE,
}

@JsonInclude(NON_EMPTY)
data class BackstageComponentSpec(
  val type: BackstageComponentSpecType,
  val lifecycle: BackstageLifecycle,
  val system: String,
  val owner: String = "hmpps-undefined",
  val dependsOn: List<String>? = null,
  val providesApis: List<String>? = null,
  val consumesApis: List<String>? = null,
)

data class BackstageComponent(
  override val metadata: BackstageMetadata,
  val spec: BackstageComponentSpec,

  @JsonIgnore
  val api: BackstageAPI? = null
) : BackstageModel(kind = COMPONENT, metadata = metadata)


