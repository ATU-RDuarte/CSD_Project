package org.atu.sse

import io.ktor.server.routing.Route
import io.ktor.server.sse.sse
import io.ktor.sse.ServerSentEvent

/**
 * Http server side event to notify car on client request for a new session
 *
 * This fires an SSE event for car to notify of an incoming session
 * requires a vuid parameter
 *
 */
fun Route.userSessionRequest() {
    sse("/userSessionRequest") {
        if (!call.parameters.contains("vuid")) {
            return@sse
        }
        send(ServerSentEvent("Requested session to ${call.parameters["vuid"]}"))
    }
}

/**
 * Http server side event to notify car on client request to end the current session
 *
 * This fires an SSE event for car to notify of the end of the current session
 * requires a vuid parameter
 *
 */
fun Route.userEndSessionRequest() {
    sse("/userEndSessionRequest") {
        if (!call.parameters.contains("vuid")) {
            return@sse
        }
        send(ServerSentEvent("End request session to ${call.parameters["vuid"]}"))
    }
}
