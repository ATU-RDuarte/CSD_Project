package org.atu

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val carMap: MutableMap<String, RsaKeyPair> = mutableMapOf()
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
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
                    text = carMap[vuid]?.let { RsaKeyHelper.publicKeyPemFormat(it.publicKey) }.toString(),
                )
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
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
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
