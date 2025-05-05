package org.atu.sse

import io.ktor.server.routing.Route
import io.ktor.server.sse.sse
import io.ktor.sse.ServerSentEvent

fun Route.userSessionRequest() {
    sse("/userSessionRequest") {
        if (!call.parameters.contains("vuid")) {
            return@sse
        }
        send(ServerSentEvent("Requested session to ${call.parameters["vuid"]}"))
    }
}

fun Route.userEndSessionRequest() {
    sse("/userEndSessionRequest") {
        if (!call.parameters.contains("vuid")) {
            return@sse
        }
        send(ServerSentEvent("End request session to ${call.parameters["vuid"]}"))
    }
}
