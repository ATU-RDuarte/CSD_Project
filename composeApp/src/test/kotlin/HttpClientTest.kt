import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.atu.SERVER_URL
import org.atu.carBuilder
import org.atu.http.AppClientHttpClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Http client for the HttpClient
 *
 * This class  contains the unit tests for Application Http Client
 *
 */
class HttpClientTest {
    private val client = AppClientHttpClient(SERVER_URL, ClientMockHttpEngine.getHttpEngine())
    private val testCar = carBuilder(vuid = "testVuid")

    /**
     * Test Application Http Client Fetch Registered Cars Request happy path
     *
     * Test that client attempts to fetch for registered cars and receives a valid response
     *
     */
    @Test
    fun testApplicationHttpClientFetchRegisteredCarsRequestHappyPath() {
        ClientMockHttpEngine.addCar(testCar.vuid, testCar)
        val response = runBlocking { client.fetchForAvailableCars() }
        assertEquals(response[0], testCar)
        ClientMockHttpEngine.removeCar(testCar.vuid)
    }

    /**
     * Test Application Http Client Requests Session Successfully
     *
     * Test that client attempts to fetch for registered cars and receives a valid response
     *
     */
    @Test
    fun testApplicationHttpClientRequestsSessionHappyPath() {
        ClientMockHttpEngine.addCar(testCar.vuid, testCar)
        val response = runBlocking { client.requestCarSession(testCar.vuid) }
        assertTrue(response.contains("modulus"))
        ClientMockHttpEngine.removeCar(testCar.vuid)
    }
}