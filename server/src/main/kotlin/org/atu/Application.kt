package org.atu

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID


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
            val carId = call.queryParameters["carId"]
            if (carId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            try {
                carMap[carId] = RsaKeyHelper.generateRsaKeyPair()
                val pem =
                    carMap[carId]?.publicKey
                call.respondText(
                    contentType = ContentType.Text.Plain,
                    text = "{\"public_key\":\"$pem\"}"
                )
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        get("/requestSession") {
            val carId = call.queryParameters["carId"]
            if (carId == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            if (!carMap.containsKey(carId)) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val carKeys = carMap[carId]
                val jwt = JWT.create()
                    .withClaim("jti", UUID.fromString(carId).toString())
                    .sign(Algorithm.RSA256(carKeys?.publicKey, carKeys?.privateKey))
                call.respondText(
                    contentType = ContentType.Application.Json,
                    text = jwt
                )
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}