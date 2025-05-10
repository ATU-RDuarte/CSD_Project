package org.atu

import http.CarClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.atu.websockets.UserSocketSession
import org.atu.websockets.WebSocketMessage

suspend fun sendResponse(
    webSocketSession: UserSocketSession,
    carStatus: String,
) {
    webSocketSession.sendMessage(Json.encodeToString(WebSocketMessage("user", carStatus)))
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
                        when (socketMessage.payload) {
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
