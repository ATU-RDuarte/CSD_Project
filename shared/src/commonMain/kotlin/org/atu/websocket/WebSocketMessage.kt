package org.atu.websocket

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketMessage(val receiver: String, val payload: String)
