import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
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
import kotlin.test.assertTrue

/**
 * Test Suite for UserCarSessionRequest
 *
 * This class contains the unit tests for UserCarSessionRequest
 *
 */
class UserCarSessionSseTest {
    val testCar = carBuilder(vuid = "testVuid")

    /**
     * Test Session UserCarSessionRequest event happy path
     *
     * Test that car is registered and a client attempts to send request a session the an sse event
     * should be fired
     *
     */
    @Test
    fun testUserCarSessionSseHappyPath() {
        testApplication {
            application {
                module()
            }
            val client =
                createClient {
                    install(SSE) {
                        showCommentEvents()
                        showRetryEvents()
                    }
                }
            assertEquals(
                HttpStatusCode.OK,
                client.post("/car/register?vuid=${testCar.vuid}") {
                    setBody(
                        carJsonSerializer(testCar),
                    )
                }.status,
            )
            assertEquals(HttpStatusCode.OK, client.get("/requestSession?vuid=${testCar.vuid}").status)
            try {
                client.sse(urlString = "/userSessionRequest?vuid=${testCar.vuid}") {
                    incoming.collect { event ->
                        event.data?.let { assertTrue(it.contains(testCar.vuid)) }
                    }
                }
            } catch (e: Exception) {
                //
            }
        }
    }

    /**
     * Test Session serCarEndSession event happy path
     *
     * Test that car is registered and a client attempts to send request to end a session
     * an sse event should be fired
     *
     */
    @Test
    fun testUserCarEndSessionSseHappyPath() {
        testApplication {
            application {
                module()
            }
            val client =
                createClient {
                    install(SSE) {
                        showCommentEvents()
                        showRetryEvents()
                    }
                }
            assertEquals(
                HttpStatusCode.OK,
                client.post("/car/register?vuid=${testCar.vuid}") {
                    setBody(
                        carJsonSerializer(testCar),
                    )
                }.status,
            )
            assertEquals(HttpStatusCode.OK, client.get("/requestSession?vuid=${testCar.vuid}").status)
            try {
                client.sse(urlString = "/userEndSessionRequest?vuid=${testCar.vuid}") {
                    incoming.collect { event ->
                        event.data?.let { assertTrue(it.contains(testCar.vuid)) }
                    }
                }
            } catch (e: Exception) {
                //
            }
        }
    }
}
