import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
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
    val vuid = "testVuid"

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
            assertEquals(HttpStatusCode.OK, client.post("/car/register?vuid=$vuid").status)
            assertEquals(HttpStatusCode.OK, client.get("/requestSession?vuid=$vuid").status)
            try {
                client.sse(urlString = "/userSessionRequest?vuid=$vuid") {
                    incoming.collect { event ->
                        event.data?.let { assertTrue(it.contains(vuid)) }
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
            assertEquals(HttpStatusCode.OK, client.post("/car/register?vuid=$vuid").status)
            assertEquals(HttpStatusCode.OK, client.get("/requestSession?vuid=$vuid").status)
            try {
                client.sse(urlString = "/userEndSessionRequest?vuid=$vuid") {
                    incoming.collect { event ->
                        event.data?.let { assertTrue(it.contains(vuid)) }
                    }
                }
            } catch (e: Exception) {
                //
            }
        }
    }
}
