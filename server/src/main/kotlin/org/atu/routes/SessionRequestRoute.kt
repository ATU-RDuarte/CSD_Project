package org.atu.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.atu.Car
import org.atu.RsaKeyPair
import org.atu.sse.userSessionRequest
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date

/**
 * Http route for client to request a car session
 *
 * This handles an http request from the client to start a new session with a car
 * requires a vuid parameter
 *
 * @param carMap internal state of registered cars
 *
 */
fun Routing.sessionRequestRoute(carMap: MutableMap<String, Pair<Car, RsaKeyPair>>) {
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
            val jwt =
                JWT.create()
                    .withExpiresAt(Date(System.currentTimeMillis() + 6000000))
                    .sign(Algorithm.RSA256((carKeys?.second?.publicKey as RSAPublicKey), (carKeys.second.privateKey as RSAPrivateKey)))
            call.respondText(
                contentType = ContentType.Application.Json,
                text = jwt,
            )
            // TODO() add as a call back on car session requested
            userSessionRequest()
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
