package org.atu.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.atu.RsaKeyHelper
import org.atu.RsaKeyPair

fun Routing.registerCar(carMap: MutableMap<String, RsaKeyPair>) {
    post("/car/register") {
        val vuid = call.queryParameters["vuid"]
        if (vuid == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        try {
            carMap[vuid] = RsaKeyHelper.generateRsaKeyPair()
            call.respondText(
                contentType = ContentType.Text.Plain,
                text =
                    carMap[vuid]?.let { RsaKeyHelper.publicKeyPemFormat(it.publicKey) }
                        .toString(),
            )
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
