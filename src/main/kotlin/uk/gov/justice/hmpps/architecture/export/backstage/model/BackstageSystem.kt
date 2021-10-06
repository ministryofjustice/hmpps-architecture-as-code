package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonIgnore
import uk.gov.justice.hmpps.architecture.export.backstage.model.BackstageModelType.SYSTEM

data class BackstageSystemSpec(
  val owner: String = "hmpps-undefined",
)

data class BackstageSystem(
  override val metadata: BackstageMetadata,
  val spec: BackstageSystemSpec = BackstageSystemSpec(),

  @JsonIgnore
  val components: List<BackstageComponent> = emptyList()
) : BackstageModel(kind = SYSTEM, metadata = metadata)


