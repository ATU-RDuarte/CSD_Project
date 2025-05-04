package org.atu

import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals

class CarClientTest {
    private val client = CarClient(CarMockHttpEngine.getHttpEngine())

    @Test
    fun testCarRegisterHappyPath() {
        val response = client.registerCar()
        assertEquals(response, HttpStatusCode.OK)
    }
}
