package org.atu

import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val client = CarClient()
    runBlocking {
        while (HttpStatusCode.OK != client.registerCar()) {
            delay(100)
        }
    }
    CoroutineScope(Dispatchers.Unconfined).launch {
        while (true) {
            client.updateCarStatus()
            delay(1000)
        }
    }
    while (true) { /**/ }
}
