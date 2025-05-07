package org.atu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.atu.Car
import org.atu.RsaKeyPair
import org.atu.sse.userEndSessionRequest

/**
 * Http route for client to request to end a car session
 *
 * This handles an http request from the client to stop the current car session
 * requires a vuid parameter
 *
 * @param carMap internal state of registered cars
 *
 */
fun Routing.endSessionRequestRoute(carMap: MutableMap<String, Pair<Car, RsaKeyPair>>) {
    get("/endSession") {
        val vuid = call.queryParameters["vuid"]
        if (vuid == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        if (!carMap.containsKey(vuid)) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        try {
            // TODO() add as a call back on car session requested
            call.respond(HttpStatusCode.OK)
            userEndSessionRequest()
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
