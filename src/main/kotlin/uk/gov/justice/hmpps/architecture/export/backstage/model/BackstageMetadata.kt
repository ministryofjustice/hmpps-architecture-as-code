package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty

enum class BackstageMetadataLinkIcon {
  @JsonProperty("github") GITHUB
}

@JsonInclude(NON_NULL)
data class BackstageMetadataLink(
  val url: String,
  val title: String,
  val icon: BackstageMetadataLinkIcon? = null
)

@JsonInclude(NON_EMPTY)
data class BackstageMetadata(
  val name: String,
  val title: String? = null,
  val description: String? = null,
  val links: List<BackstageMetadataLink>? = null,
  val annotations: Map<String, String>? = null,
)
