package org.atu

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test Suite for CarStatusRoute
 *
 * This class contains the unit tests for the Car Status update route
 *
 */
class CarStatusUpdateTest {
    private val testCar = carBuilder(vuid = "carUnderTest")

    private suspend fun registerCar(client: HttpClient): HttpStatusCode =
        client.post("/car/register?vuid=${testCar.vuid}") {
            setBody(
                carJsonSerializer(testCar),
            )
        }.status

    /**
     * Test Car Update Happy path
     *
     * Test that car sends a POST request with a vuid parameter and a json serialized car body
     * to /carStatus and receives a valid response
     *
     */
    @Test
    fun testCarStatusUpdateHappyPath() {
        testApplication {
            application {
                module()
            }
            assertEquals(registerCar(client), HttpStatusCode.OK)
            assertEquals(
                client.post("/carStatus?vuid=${testCar.vuid}") {
                    setBody(carJsonSerializer(testCar))
                }.status,
                HttpStatusCode.OK,
            )
        }
    }

    /**
     * Test Car Update Without A Vuid
     *
     * Test that car sends a POST request without a vuid parameter and a json serialized car body
     * to /carStatus and receives a BadRequest Response
     *
     */
    @Test
    fun testCarStatusUpdateWithoutVuid() {
        testApplication {
            application {
                module()
            }
            assertEquals(registerCar(client), HttpStatusCode.OK)
            assertEquals(
                client.post("/carStatus") {
                    setBody(carJsonSerializer(testCar))
                }.status,
                HttpStatusCode.BadRequest,
            )
        }
    }

    /**
     * Test Car Update Without For A Non Registered Car
     *
     * Test that car sends a POST request with a non existing vuid parameter and a json serialized
     * car body to /carStatus and receives a BadRequest Response
     *
     */
    @Test
    fun testCarStatusUpdateForUnRegisteredCar() {
        testApplication {
            application {
                module()
            }
            assertEquals(
                client.post("/carStatus?vuid=${testCar.vuid}") {
                    setBody(carJsonSerializer(testCar))
                }.status,
                HttpStatusCode.BadRequest,
            )
        }
    }

    /**
     * Test Car Update With Wrong Method
     *
     * Test that car sends a GET request with a vuid parameter and a json serialized car body
     * to /carStatus and receives a MethodNotAllowed Response
     *
     */
    @Test
    fun testCarStatusUpdateWithWrongMethod() {
        testApplication {
            application {
                module()
            }
            assertEquals(registerCar(client), HttpStatusCode.OK)
            assertEquals(
                client.get("/carStatus?vuid=${testCar.vuid}") {
                    setBody(carJsonSerializer(testCar))
                }.status,
                HttpStatusCode.MethodNotAllowed,
            )
        }
    }
}
