import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import org.atu.Car
import org.atu.RsaKeyHelper
import org.atu.RsaKeyPair
import org.atu.carJsonSerializer

fun carListToJson(carList : List<Car>): String {
    var jsonCarArray = ""
    carList.forEach { car -> jsonCarArray += "${carJsonSerializer(car)}," }
    return if (jsonCarArray.isEmpty()) "[$jsonCarArray]" else "[${jsonCarArray.dropLast(1)}]"
}

object ClientMockHttpEngine {
    private val carMap: MutableMap<String, Pair<Car, RsaKeyPair>> = mutableMapOf()
    private val mockEngine: MockEngine =
        MockEngine { request ->
            when (request.method) {
                HttpMethod.Get -> {
                    if (request.url.fullPath.contains("/fetchRegisteredCars")) {
                        respond(carListToJson(carMap.values.toMap().keys.toList()), HttpStatusCode.OK)
                    } else {
                        respond("", HttpStatusCode.BadRequest)
                    }
                }
                else -> respond("", HttpStatusCode.BadRequest)
            }
        }

    fun getHttpEngine() = mockEngine

    fun addCar(vuid: String, car: Car)
    {
        carMap[vuid] = car to RsaKeyHelper.generateRsaKeyPair()
    }

    fun removeCar(vuid: String)
    {
        carMap.remove(vuid)
    }
}