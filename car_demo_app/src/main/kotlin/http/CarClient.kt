package http

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import org.atu.Car
import org.atu.RsaKeyHelper
import org.atu.SERVER_URL
import org.atu.carBuilder
import org.atu.carJsonSerializer
import java.security.interfaces.RSAPublicKey

/**
 * Http client for the car application
 *
 * This class handles http communication with the server
 *
 */
class CarClient(
    httpClientEngine: HttpClientEngine = CIO.create(),
    private val carStatus: Car = carBuilder(),
) {
    private lateinit var currentPublicKey: RSAPublicKey
    var doorLocked = true
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

    suspend fun registerCar(): HttpStatusCode {
        try {
            val response =
                client.post(
                    "$SERVER_URL/car/register",
                ) {
                    url {
                        parameters.append("vuid", carStatus.vuid)
                    }
                    setBody(
                        carJsonSerializer(carStatus),
                    )
                }
            println("Attempting to register car with vuid: ${carStatus.vuid}")
            if (response.status != HttpStatusCode.OK) {
                println("Failed to register car with error code ${response.status}")
                return response.status
            }
            val keyPem = response.bodyAsText()
            currentPublicKey = RsaKeyHelper.pemToRsaPublicKey(keyPem)
            println("Got successfully response, received key: $keyPem")
            return response.status
        } catch (e: Exception) {
            return HttpStatusCode.BadGateway
        }
    }

    suspend fun updateCarStatus(): HttpStatusCode {
        return try {
            client.post("$SERVER_URL/carStatus") {
                url {
                    parameters.append("vuid", carStatus.vuid)
                }
                setBody(
                    carJsonSerializer(carStatus),
                )
            }.status
        } catch (e: Exception) {
            HttpStatusCode.BadGateway
        }
    }

    fun getHttpClient() = client

    fun getCarStatus() = carStatus
}
