package org.atu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.atu.RsaKeyPair
import org.atu.sse.userEndSessionRequest

fun Routing.endSessionRequestRoute(carMap: MutableMap<String, RsaKeyPair>) {
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
