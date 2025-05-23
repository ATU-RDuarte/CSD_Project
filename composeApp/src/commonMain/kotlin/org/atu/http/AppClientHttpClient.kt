package org.atu.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import org.atu.Car
import org.atu.jsonCarParser

/**
 * Http client for the Client application
 *
 * This class handles http communication with the server
 *
 * @param serverUrl server url (differs for android client)
 * @param httpClientEngine http client engine dependency injection
 *
 */
class AppClientHttpClient(private val serverUrl: String, httpClientEngine: HttpClientEngine) {
    private val client: HttpClient =
        HttpClient(httpClientEngine) {
            expectSuccess = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                filter { request ->
                    request.url.host.contains("ktor.io")
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000
            }
        }

    suspend fun fetchForAvailableCars(): List<Car> {
        try {
            val response = client.get("$serverUrl/fetchRegisteredCars")
            println("Requested available cars to server")
            if (response.status != HttpStatusCode.OK) {
                println("Request failed with error ${response.status}")
                return listOf()
            }
            val carListString = response.bodyAsText().drop(1).dropLast(1)
            val carList = mutableListOf<Car>()
            carListString.split("},").forEach { car -> carList.add(jsonCarParser(car)) }
            return carList
        } catch (e: Exception) {
            println("Caught exception ${e.message}")
            return listOf()
        }
    }

    suspend fun requestCarSession(vuid: String): String {
        try {
            val response = client.get("$serverUrl/requestSession?vuid=$vuid")
            println("Requested session for cars $vuid")
            if (response.status != HttpStatusCode.OK) {
                println("Request failed with error ${response.status}")
                return ""
            }
            val base64CarKey =
                response.bodyAsText().split(":")[1].replace("\"", "").replace(" ", "")
            println("Base64 encoded key $base64CarKey")
            return base64CarKey
        } catch (e: Exception) {
            println("Caught exception ${e.message}")
            return ""
        }
    }

    fun getWebSocketUrl() = serverUrl.replace("http", "ws")
    fun getHttpClient() = client
}