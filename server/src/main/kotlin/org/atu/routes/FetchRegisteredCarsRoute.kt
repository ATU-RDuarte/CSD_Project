package org.atu.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.atu.Car
import org.atu.RsaKeyPair
import org.atu.carJsonSerializer
import java.util.concurrent.ConcurrentHashMap

/**
 * Http route for client to request for data from registered cars
 *
 * This handles an http request from the client to fetch data from registered cars
 *
 * @param carMap internal state of registered cars
 *
 */
fun Routing.fetchRegisteredCarsRoute(carMap: ConcurrentHashMap<String, Pair<Car, RsaKeyPair>>) {
    get("/fetchRegisteredCars") {
        try {
            // TODO() add as a call back on car session requested
            var jsonCarArray = ""
            carMap.values.forEach { pair -> jsonCarArray += "${carJsonSerializer(pair.first)}," }
            jsonCarArray = if (jsonCarArray.isEmpty()) "[$jsonCarArray]" else "[${jsonCarArray.dropLast(1)}]"
            call.respondText(
                contentType = ContentType.Application.Json,
                text = jsonCarArray,
            )
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
