package org.atu

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test Suite for RegisterCarRoute
 *
 * This class contains the unit tests for RegisterCarRoute
 *
 */
class RegisterCarTest {
    private val testCar = carBuilder(vuid = "testVuid")

    /**
     * Test Car Registration happy path
     *
     * Test that client sends a POST request with a vuid parameter to /car/register and
     * receives a valid response
     *
     */
    @Test
    fun testCarRegisterHappyPath() {
        testApplication {
            application {
                module()
            }
            val response =
                client.post("/car/register") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                    setBody(carJsonSerializer(testCar))
                }
            assertEquals(response.status, HttpStatusCode.OK)
            assertTrue(response.call.response.bodyAsText().contains(("-----BEGIN PUBLIC KEY-----")))
            assertTrue(response.call.response.bodyAsText().contains(("-----END PUBLIC KEY-----")))
        }
    }

    /**
     * Test Car Registration without vuid
     *
     * Test that client sends a POST request without a vuid parameter to /car/register and
     * receives a BadRequest response
     *
     */
    @Test
    fun testCarRegisterNoVuidParameter() {
        testApplication {
            application {
                module()
            }
            val response = client.post("/car/register")
            assertEquals(response.status, HttpStatusCode.BadRequest)
        }
    }

    /**
     * Test Car Registration without vuid parameter
     *
     * Test that client sends a GET request with a vuid parameter to /car/register and
     * receives a MethodNotAllowed response
     *
     */
    @Test
    fun testCarRegisterInvalidMethod() {
        testApplication {
            application {
                module()
            }
            val response =
                client.get("/car/register") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                    setBody(carJsonSerializer(testCar))
                }
            assertEquals(response.status, HttpStatusCode.MethodNotAllowed)
        }
    }
}
