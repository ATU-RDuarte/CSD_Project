package org.atu.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.atu.RsaKeyPair
import org.atu.sse.userSessionRequest
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date

fun Routing.sessionRequestRoute(carMap: MutableMap<String, RsaKeyPair>) {
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
                    .sign(Algorithm.RSA256((carKeys?.publicKey as RSAPublicKey), (carKeys.privateKey as RSAPrivateKey)))
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
