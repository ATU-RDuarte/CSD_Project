package org.atu

import kotlinx.coroutines.*

fun main()
{
    val client = CarClient()
    runBlocking {
        client.registerCar()
    }
}