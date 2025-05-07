import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.atu.carBuilder
import org.atu.carJsonSerializer
import org.atu.jsonCarParser
import org.atu.module
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test Suite for FetchRegisteredCarsRoute
 *
 * This class contains the unit tests for FetchRegisteredCars
 *
 */
class FetchRegisteredCarsTest {
    private val testCar = carBuilder(vuid = "testVuid")

    /**
     * Test Car Fetch Registered Cars happy path
     *
     * Test that client sends a get request to /fetchAvailableCars and
     * receives a json list of registered cars
     *
     */
    @Test
    fun testFetchRegisteredCarsHappyPath() {
        testApplication {
            application {
                module()
            }
            assertEquals(
                client.post("/car/register") {
                    url {
                        parameters.append("vuid", testCar.vuid)
                    }
                    setBody(carJsonSerializer(testCar))
                }.status,
                HttpStatusCode.OK,
            )
            val response = client.get("/fetchAvailableCars")
            val carFromJsonArray = jsonCarParser(response.bodyAsText().drop(1).dropLast(1))
            assertEquals(carFromJsonArray.vuid, testCar.vuid)
            assertEquals(carFromJsonArray.availability, testCar.availability)
            assertEquals(carFromJsonArray.fuel, testCar.fuel)
            assertEquals(carFromJsonArray.price, testCar.price)
        }
    }

    /**
     * Test Car Fetch Registered Cars happy path
     *
     * Test that client sends a get request to /fetchAvailableCars and without any registered car
     * receives a json empty list
     *
     */
    @Test
    fun testFetchRegisteredCarsNoCarRegistered() {
        testApplication {
            application {
                module()
            }
            val response = client.get("/fetchAvailableCars")
            assertEquals(response.bodyAsText(), "[]")
        }
    }
}
