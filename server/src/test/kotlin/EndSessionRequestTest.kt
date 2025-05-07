import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.atu.carBuilder
import org.atu.carJsonSerializer
import org.atu.module
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test Suite for EndSessionRequestRoute
 *
 * This class contains the unit tests for EndSessionRequestRoute
 *
 */
class EndSessionRequestTest {
    private val testCar = carBuilder(vuid = "testVuid")

    /**
     * Test End Session Request happy path
     *
     * Test that client sends a GET request with a vuid parameter to /endSession and
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
                client.get("/endSession") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                }
            assertEquals(response.status, HttpStatusCode.OK)
        }
    }

    /**
     * Test End Session Request to non registered car
     *
     * Test that client sends a GET request with a vuid (not registered) parameter
     * to /endSession and receives a BadRequest response
     *
     */
    @Test
    fun testSessionRequestNonRegisteredCarHappyPath() {
        val vuid = "testVuid"
        testApplication {
            application {
                module()
            }
            val response =
                client.get("/endSession") {
                    url {
                        parameters.append("vuid", vuid)
                    }
                }
            assertEquals(response.status, HttpStatusCode.BadRequest)
        }
    }

    /**
     * Test End Session Request without vuid parameter
     *
     * Test that client sends a GET request with a vuid parameter to /endSession and
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
            val response = client.get("/endSession")
            assertEquals(response.status, HttpStatusCode.BadRequest)
        }
    }

    /**
     * Test End Session Request invalid http method
     *
     * Test that client sends a GET request with a vuid parameter to /endSession and
     * receives a MethodNotAllowed response
     *
     */
    @Test
    fun testSessionRequestTestInvalidMethod() {
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
                client.post("/endSession") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                }
            assertEquals(response.status, HttpStatusCode.MethodNotAllowed)
        }
    }
}
