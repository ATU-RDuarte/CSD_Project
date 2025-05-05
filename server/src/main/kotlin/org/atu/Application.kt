package org.atu

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.sse.SSE
import org.atu.routes.endSessionRequestRoute
import org.atu.routes.registerCar
import org.atu.routes.sessionRequestRoute

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(SSE)
    // TODO Store into database and fetch from db
    val carMap: MutableMap<String, RsaKeyPair> = mutableMapOf()
    routing {
        registerCar(carMap)
        sessionRequestRoute(carMap)
        endSessionRequestRoute(carMap)
    }
}
