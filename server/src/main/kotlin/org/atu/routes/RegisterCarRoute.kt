package org.atu.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.atu.Car
import org.atu.RsaKeyHelper
import org.atu.RsaKeyPair
import org.atu.jsonCarParser

/**
 * Http route for a car to request register
 *
 * This handles an http communication from the car to register it self
 * requires a vuid parameter
 *
 * @param carMap internal state of registered cars
 *
 */
fun Routing.registerCar(carMap: MutableMap<String, Pair<Car, RsaKeyPair>>) {
    post("/car/register") {
        val vuid = call.queryParameters["vuid"]
        if (vuid == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val car = jsonCarParser(call.receiveText())
        try {
            carMap[vuid] = car to RsaKeyHelper.generateRsaKeyPair()
            call.respondText(
                contentType = ContentType.Text.Plain,
                text =
                    carMap[vuid]?.let { RsaKeyHelper.publicKeyPemFormat(it.second.publicKey) }
                        .toString(),
            )
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
