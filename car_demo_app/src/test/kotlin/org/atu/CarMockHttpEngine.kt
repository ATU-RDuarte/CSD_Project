package org.atu

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath

internal lateinit var rsaKeyPair: RsaKeyPair

object CarMockHttpEngine {
    private val mockEngine: MockEngine = MockEngine { request ->
        when (request.method) {
            HttpMethod.Post -> {
                println("Got a POST request")
                if (request.url.fullPath.contains("car/register")) {
                    println("POST request contains car/register")
                    if (request.url.parameters.contains("vuid")) {
                        println("POST request contains vuid")
                        rsaKeyPair = RsaKeyHelper.generateRsaKeyPair()
                        respond(RsaKeyHelper.publicKeyPemFormat(rsaKeyPair.publicKey), HttpStatusCode.OK)
                    }
                    else respond("", HttpStatusCode.BadRequest)
                }
                else respond("", HttpStatusCode.BadRequest)
            }
            else -> respond("", HttpStatusCode.BadRequest)
        }
    }

    fun getHttpEngine() = mockEngine
}