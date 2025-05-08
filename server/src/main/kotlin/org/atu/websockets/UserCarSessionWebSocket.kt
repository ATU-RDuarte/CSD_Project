package org.atu.websockets

import ServerSessionState
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import org.atu.Car
import org.atu.RsaKeyPair
import org.atu.websocket.WebSocketMessage
import java.util.concurrent.ConcurrentHashMap

/**
 * Websocket route to handle user car session
 *
 * This handles websocket sessions for the user and the car
 *
 * @param carMap map of registered cars
 *
 */
fun Route.userCarSessionWebSocket(carMap: ConcurrentHashMap<String, Pair<Car, RsaKeyPair>>) {
    webSocket("/carSessionChannel/{vuid}") {
        if (call.parameters["vuid"] !in carMap.keys) {
            this.close(
                CloseReason(
                    CloseReason.Codes.CANNOT_ACCEPT,
                    "${call.parameters["vuid"]} not registered",
                ),
            )
            return@webSocket
        }

        val entity =
            call.request.queryParameters["entity"] ?: run {
                this.close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "An entity 'car' or 'user' must be provided",
                    ),
                )
                return@webSocket
            }

        if (!(entity == "car" || entity == "user")) {
            this.close(
                CloseReason(
                    CloseReason.Codes.CANNOT_ACCEPT,
                    "Invalid entity '$entity'",
                ),
            )
        }

        ServerSessionState.carSessionMap[entity] = this
        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val incoming = Json.decodeFromString<WebSocketMessage>(frame.readText())
                    if (incoming.receiver in ServerSessionState.carSessionMap.keys) {
                        ServerSessionState.carSessionMap[incoming.receiver]?.send(frame)
                    }
                }
            }
        } finally {
            // TODO() replace with actual report
            ServerSessionState.carSessionMap["user"]?.send(Frame.Text("Final REPORT"))
            ServerSessionState.carSessionMap.remove(entity)
            this.close()
        }
    }
}
