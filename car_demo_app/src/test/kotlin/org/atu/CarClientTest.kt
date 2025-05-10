package org.atu

import http.CarClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Http client for the CarClient
 *
 * This class  contains the unit tests for Car Http Client
 *
 */
class CarClientTest {
    private val client = CarClient(CarMockHttpEngine.getHttpEngine())

    /**
     * Test Car Http Client Register Request happy path
     *
     * Test that client attempts to register and receives a valid response
     *
     */
    @Test
    fun testCarRegisterHappyPath() {
        val response = runBlocking { client.registerCar() }
        assertEquals(response, HttpStatusCode.OK)
    }
}
