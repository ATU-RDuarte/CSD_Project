package org.atu.websockets

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.isActive

/**
 * Client User Application Socket Session Class
 *
 * Class handles lifecycle and handling of websocket session
 *
 * @param httpClient http client used to create websocket session
 *
 */
class UserSocketSession(private val httpClient: HttpClient) {
    private var socket: WebSocketSession? = null

    suspend fun initSocket(urlString: String): Boolean {
        try {
            socket =
                httpClient.webSocketSession(urlString = urlString) {
                }
            return socket!!.isActive
        } catch (e: Exception) {
            println("Failed to initialize socket: ${e.message}")
            return false
        }
    }

    fun isSocketConnected(): Boolean {
        return socket != null && socket!!.isActive
    }

    suspend fun sendMessage(message: String): Boolean {
        if (socket == null) return false
        socket!!.send(Frame.Text(message))
        return true
    }

    suspend fun receiveMessage(): String? {
        if (socket == null) return null
        val message = socket!!.incoming.receive()
        if (message !is Frame.Text) return null
        return message.readText()
    }

    suspend fun closeSession() {
        socket?.close()
    }
}
