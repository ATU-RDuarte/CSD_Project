package org.atu

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test Suite for SessionRequestRoute
 *
 * This class contains the unit tests for SessionRequestRoute
 *
 */
class SessionRequestTest {
    private val testCar = carBuilder(vuid = "testVuid")

    /**
     * Test Session Request happy path
     *
     * Test that client sends a GET request with a vuid parameter to /requestSession and
     * receives a valid response
     *
     */
    @Test
    fun testSessionRequestHappyPath() {
        testApplication {
            application {
                module()
            }
            assertEquals(
                HttpStatusCode.OK,
                client.post("/car/register?vuid=${testCar.vuid}") {
                    setBody(
                        carJsonSerializer(testCar),
                    )
                }.status,
            )
            val response =
                client.get("/requestSession") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                }
            assertEquals(response.status, HttpStatusCode.OK)
        }
    }

    /**
     * Test Session Request to non registered car
     *
     * Test that client sends a GET request with a vuid (not registered) parameter
     * to /requestSession and receives a BadRequest response
     *
     */
    @Test
    fun testSessionRequestNonRegisteredCar() {
        val vuid = "testVuid"
        testApplication {
            application {
                module()
            }
            val response =
                client.get("/requestSession") {
                    url {
                        parameters.append("vuid", vuid)
                    }
                }
            assertEquals(response.status, HttpStatusCode.BadRequest)
        }
    }

    /**
     * Test Session Request without vuid parameter
     *
     * Test that client sends a GET request with a vuid parameter to /requestSession and
     * receives a BadRequest response
     *
     */
    @Test
    fun testSessionRequestTestNoVuidParameter() {
        testApplication {
            application {
                module()
            }
            assertEquals(
                HttpStatusCode.OK,
                client.post("/car/register?vuid=${testCar.vuid}") {
                    setBody(
                        carJsonSerializer(testCar),
                    )
                }.status,
            )
            val response = client.get("/requestSession")
            assertEquals(response.status, HttpStatusCode.BadRequest)
        }
    }

    /**
     * Test Session Request invalid http method
     *
     * Test that client sends a GET request with a vuid parameter to /car/register and
     * receives a MethodNotAllowed response
     *
     */
    @Test
    fun testSessionRequestTestInvalidMethod() {
        val vuid = "testVuid"
        testApplication {
            application {
                module()
            }
            assertEquals(
                HttpStatusCode.OK,
                client.post("/car/register?vuid=${testCar.vuid}") {
                    setBody(
                        carJsonSerializer(testCar),
                    )
                }.status,
            )
            val response =
                client.post("/requestSession") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                }
            assertEquals(response.status, HttpStatusCode.MethodNotAllowed)
        }
    }
}
