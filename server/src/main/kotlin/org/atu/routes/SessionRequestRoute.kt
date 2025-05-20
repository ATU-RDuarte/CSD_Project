package org.atu.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.atu.Car
import org.atu.RsaKeyHelper.bytesToBase64
import org.atu.RsaKeyPair
import java.util.concurrent.ConcurrentHashMap

/**
 * Http route for client to request a car session
 *
 * This handles an http request from the client to start a new session with a car
 * requires a vuid parameter
 *
 * @param carMap internal state of registered cars
 *
 */
fun Routing.sessionRequestRoute(carMap: ConcurrentHashMap<String, Pair<Car, RsaKeyPair>>) {
    get("/requestSession") {
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
            val carKeys = carMap[vuid]
            val encodedCarPrivateKey = carKeys!!.second.privateKey.encoded
            call.respondText(
                contentType = ContentType.Application.Json,
                text = "\"car_key\":\"${bytesToBase64(encodedCarPrivateKey)}\"",
            )
            // TODO() add as a call back on car session requested
            // TODO() Terminate websocket
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
