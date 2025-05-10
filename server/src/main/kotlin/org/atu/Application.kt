package org.atu

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import org.atu.routes.carStatusRoute
import org.atu.routes.endSessionRequestRoute
import org.atu.routes.fetchRegisteredCarsRoute
import org.atu.routes.registerCar
import org.atu.routes.sessionRequestRoute
import org.atu.websockets.userCarSessionWebSocket
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

/**
 * Application module for the server
 *
 * Handles installation of necessary server side plugins and defines server routes
 *
 */
fun Application.module() {
    install(WebSockets) {
        pingPeriod = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    // TODO Store into database and fetch from db
    val carMap = ConcurrentHashMap<String, Pair<Car, RsaKeyPair>>()
    routing {
        registerCar(carMap)
        fetchRegisteredCarsRoute(carMap)
        sessionRequestRoute(carMap)
        endSessionRequestRoute(carMap)
        carStatusRoute(carMap)
        userCarSessionWebSocket(carMap)
    }
}
