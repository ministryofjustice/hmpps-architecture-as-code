package uk.gov.justice.hmpps.architecture.export.backstage.apispec

import okhttp3.OkHttpClient
import okhttp3.Request

class WebClient {
  private val client: OkHttpClient = OkHttpClient()
    .newBuilder()
    .followRedirects(false)
    .build()

  fun pageExists(url: String): Boolean {
    return client.newCall(Request.Builder().url(url).build()).execute().isSuccessful
  }
}
