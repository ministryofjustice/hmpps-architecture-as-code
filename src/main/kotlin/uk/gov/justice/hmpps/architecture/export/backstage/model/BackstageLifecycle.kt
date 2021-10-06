package uk.gov.justice.hmpps.architecture.export.backstage.model

import com.fasterxml.jackson.annotation.JsonProperty

enum class BackstageLifecycle {
  @JsonProperty("deprecated") DEPRECATED,
  @JsonProperty("production") PRODUCTION,
}
