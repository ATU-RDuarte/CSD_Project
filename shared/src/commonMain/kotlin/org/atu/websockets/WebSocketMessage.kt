package org.atu.websockets

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketMessage(val receiver: String, val payload: String)
