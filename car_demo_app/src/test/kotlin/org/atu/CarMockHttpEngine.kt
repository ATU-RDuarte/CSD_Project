package org.atu

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath

internal lateinit var rsaKeyPair: RsaKeyPair

object CarMockHttpEngine {
    private val mockEngine: MockEngine =
        MockEngine { request ->
            when (request.method) {
                HttpMethod.Post -> {
                    println("Got a POST request")
                    if (request.url.parameters.contains("vuid")) {
                        if (request.url.fullPath.contains("car/register")) {
                            println("POST request contains car/register")
                            println("POST request contains vuid")
                            rsaKeyPair = RsaKeyHelper.generateRsaKeyPair()
                            respond(RsaKeyHelper.publicKeyPemFormat(rsaKeyPair.publicKey), HttpStatusCode.OK)
                        } else if (request.url.fullPath.contains("/carStatus")) {
                            respondOk()
                        } else {
                            respondBadRequest()
                        }
                    } else {
                        respondBadRequest()
                    }
                }
                else -> respondBadRequest()
            }
        }

    fun getHttpEngine() = mockEngine
}
