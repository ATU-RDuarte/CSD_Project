package org.atu.http

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.isActive

class UserSocketSession(private val httpClient: HttpClient) {
    private var socket: WebSocketSession? = null
    suspend fun initSocket(urlString: String): Boolean {
        try {
            socket = httpClient.webSocketSession(urlString = urlString) {

            }
            return socket!!.isActive
        }catch (e: Exception) {
            println("Failed to initialize socket: ${e.message}")
            return false
        }
    }

    fun isSocketActive() = socket != null && socket!!.isActive

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
        println("Last message: ${socket!!.incoming.receive().readBytes()}")
    }

}