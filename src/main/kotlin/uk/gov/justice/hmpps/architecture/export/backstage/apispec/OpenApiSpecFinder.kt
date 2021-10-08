package uk.gov.justice.hmpps.architecture.export.backstage.apispec

class OpenApiSpecFinder {

  private val specRegex = "/swagger-ui.*".toRegex()
  private val unhostedUiRegex = "https://editor.swagger.io/\\?url=(.*)".toRegex()

  fun deriveApiSpecFor(swaggerUiUrl: String): String? {

    if (unhostedUiRegex.matches(swaggerUiUrl)) {
      return unhostedUiRegex.matchEntire(swaggerUiUrl)?.groupValues?.get(1)
    }

    return specRegex.replace(swaggerUiUrl, "/v3/api-docs")
  }
}
