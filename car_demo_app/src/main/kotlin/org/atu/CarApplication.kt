package org.atu

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.post
import io.ktor.http.*
import kotlinx.coroutines.*

suspend fun registerCar(client: HttpClient, carId: String) = coroutineScope {
    launch {
         client.post(
           "http://0.0.0.0:8080/car/register"
        ){
            url{
                parameters.append("carId", carId)
            }
        }
    }
}

fun main()
{
    val client = HttpClient()
    {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            filter { request ->
                request.url.host.contains("ktor.io")
            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }
}