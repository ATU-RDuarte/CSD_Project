package org.atu

import kotlinx.coroutines.runBlocking

fun main() {
    val client = CarClient()
    runBlocking {
        // TODO() attempt to register on failure
        client.registerCar()
        // TODO() only subscribe on register
        while (!client.subscribeToSessionEvents()) { /**/ }
    }
}
