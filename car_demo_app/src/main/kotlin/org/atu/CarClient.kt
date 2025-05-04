package org.atu

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import java.security.interfaces.RSAPublicKey

class CarClient(
    httpClientEngine: HttpClientEngine = CIO.create(),
    private val carStatus: Car = carBuilder(),
) {
    private lateinit var currentPublicKey: RSAPublicKey
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
        }

    fun registerCar(): HttpStatusCode {
        return runBlocking {
            val response =
                client.post(
                    "$SERVER_URL/car/register",
                ) {
                    url {
                        parameters.append("vuid", carStatus.vuid)
                    }
                }
            if (response.status != HttpStatusCode.OK) {
                println("Failed to register car with error code ${response.status}")
                return@runBlocking response.status
            }
            val keyPem = response.bodyAsText()
            currentPublicKey = RsaKeyHelper.pemToRsaPublicKey(keyPem)
            println("Got successfully response, received key: $keyPem")
            return@runBlocking response.status
        }
    }

    fun getCarStatus() = carStatus
}
