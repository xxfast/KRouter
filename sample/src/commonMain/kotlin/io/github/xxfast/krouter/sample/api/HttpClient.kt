package io.github.xxfast.krouter.sample.api

import io.github.xxfast.krouter.sample.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val HttpClient = HttpClient(CIO) {
  install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
  install(Logging) {
    logger = object : Logger {
      override fun log(message: String) {
        println(message)
      }
    }
    level = LogLevel.ALL
  }

  defaultRequest {
    url {
      host = "api.nytimes.com"
      protocol = URLProtocol.HTTPS
      parameters.append("api-key", BuildKonfig.API_KEY)
    }
  }
}


suspend inline fun <reified T> HttpClient.get(
  block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = request {
  get { block() }
}

suspend inline fun <reified T> request(
  requester: () -> HttpResponse
): Result<T> = try {
  val httpResponse: HttpResponse = requester()
  val response: T = httpResponse.body()
  Result.success(response)
} catch (exception: ResponseException) {
  Result.failure(exception)
} catch (exception: Throwable) {
  exception.printStackTrace()
  Result.failure(exception)
}
