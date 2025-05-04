package org.atu

import kotlinx.coroutines.runBlocking

fun main() {
    val client = CarClient()
    runBlocking {
        client.registerCar()
    }
}
