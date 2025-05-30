package org.atu

import http.CarClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.atu.RsaKeyHelper.base64UrlToBytes
import org.atu.websockets.UserSocketSession
import org.atu.websockets.WebSocketMessage
import java.security.interfaces.RSAPublicKey

suspend fun sendResponse(
    webSocketSession: UserSocketSession,
    carStatus: String,
) {
    webSocketSession.sendMessage(Json.encodeToString(WebSocketMessage("user", carStatus)))
}

fun getDecodedRequest(
    message: String,
    carRSAPublicKey: RSAPublicKey,
): String {
    val jwtPayload =
        validateSelfSignedJwt(
            message,
            carRSAPublicKey,
        )
    println("GOT ENCODED JWT PAYLOAD $jwtPayload")
    val messageAsJson = Json.parseToJsonElement(base64UrlToBytes(jwtPayload).decodeToString())
    return messageAsJson.jsonObject["request"].toString()
}

fun main() {
    val client = CarClient()
    runBlocking {
        while (HttpStatusCode.OK != client.registerCar()) {
            delay(100)
        }
    }
    val socket = UserSocketSession(client.getHttpClient())
    val url = SERVER_URL.replace("http", "ws")
    CoroutineScope(Dispatchers.Unconfined).launch {
        while (true) {
            client.updateCarStatus()
            delay(100)
        }
    }

    while (true) {
        runBlocking {
            if (!socket.isSocketConnected()) {
                socket.initSocket("$url/carSessionChannel/${client.getCarStatus().vuid}?entity=car")
                client.updateCarStatus()
                delay(100)
            } else {
                try {
                    val message = socket.receiveMessage()
                    if (message != null) {
                        val socketMessage = Json.decodeFromString<WebSocketMessage>(message)
                        val decodedCommand =
                            getDecodedRequest(socketMessage.payload, client.getCurrentPublicKey())
                        println("DECODED JWT $decodedCommand")
                        when (decodedCommand.replace("\"", "")) {
                            "LOCK_CAR" -> {
                                client.doorLocked = true
                                sendResponse(socket, "CAR_LOCKED")
                            }

                            "UNLOCK_CAR" -> {
                                client.doorLocked = false
                                sendResponse(socket, "CAR_UNLOCKED")
                            }

                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    println("Got exception ${e.message}")
                }
            }
        }
    }
}
