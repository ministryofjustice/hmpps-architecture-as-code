package uk.gov.justice.hmpps.architecture.export.backstage.apispec

class OpenApiSpecFinder(private val client: WebClient = WebClient()) {

  private val specRegex = "/swagger-ui.*".toRegex()
  private val unhostedUiRegex = "https://editor.swagger.io/\\?url=(.*)".toRegex()
  private val specVariants = listOf(
    "/v3/api-docs",
    "/api/swagger.json",
    "/v2/api-docs",
    "/v2/api-docs?group=Community%20API"
  )

  fun findOpenApiSpecFor(swaggerUiUrl: String): String? {

    println("API Spec: $swaggerUiUrl")

    if (unhostedUiRegex.matches(swaggerUiUrl)) {
      return unhostedUiRegex.matchEntire(swaggerUiUrl)?.groupValues?.get(1)
    }

    return specVariants
      .map { specRegex.replace(swaggerUiUrl, it) }
      .firstOrNull { variant -> client.pageExists(variant) }
  }
}
