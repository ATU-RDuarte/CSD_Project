package org.atu.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.atu.Car
import org.atu.RsaKeyPair
import org.atu.jsonCarParser
import java.util.concurrent.ConcurrentHashMap

/**
 * Http route for car client to update status
 *
 * This handles an http request from the car to update current status
 *
 * @param carMap internal state of registered cars
 *
 */
fun Route.carStatusRoute(carMap: ConcurrentHashMap<String, Pair<Car, RsaKeyPair>>) {
    post("/carStatus") {
        val vuid = call.queryParameters["vuid"]
        if (vuid == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (!carMap.containsKey(vuid)) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        try {
            // TODO() add as a call back on car session requested
            val deserializedCar = jsonCarParser(call.receiveText())
            val oldCarStatus = carMap[vuid]
            carMap[vuid] = deserializedCar to oldCarStatus!!.second
            call.respond(HttpStatusCode.OK)
            // TODO() Terminate websocket
        } catch (ex: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
