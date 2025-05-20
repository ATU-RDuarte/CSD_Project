package org.atu

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.serialization.json.Json
import org.atu.websockets.WebSocketMessage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test Suite for UserCarSessionWebSocket
 *
 * This class contains the unit tests for the User Car session websocket
 *
 */
class UserCarSessionWebSocketTest {
    private val testCar = carBuilder(vuid = "carUnderTest")

    private suspend fun registerCar(client: HttpClient): HttpStatusCode =
        client.post("/car/register?vuid=${testCar.vuid}") {
            setBody(
                carJsonSerializer(testCar),
            )
        }.status

    /**
     * Test WebSocket happy path
     *
     * Test that car connects to the websocket using valid parameters and can send
     * and receive message
     *
     */
    @Test
    fun testWebSocketHappyPath() {
        testApplication {
            application {
                module()
            }

            val carClient =
                createClient {
                    install(WebSockets)
                }

            assertEquals(registerCar(carClient), HttpStatusCode.OK)
            carClient.webSocket("/carSessionChannel/${testCar.vuid}?entity=car") {
                send(Frame.Text(Json.encodeToString(WebSocketMessage("car", "hello"))))
                val responseText = (incoming.receive() as Frame.Text).readText()
                assertTrue(responseText.contains("car"))
            }
        }
    }

    /**
     * Test WebSocket can't connect due to invalid parameters (example unregistered car)
     *
     * Test that car attempts to connect to the websocket using invalid parameters
     * and connection is refused
     *
     */
    @Test
    fun testWebSocketInvalidParametersOrNonRegisteredCar() {
        testApplication {
            application {
                module()
            }

            val carClient =
                createClient {
                    install(WebSockets)
                }

            carClient.webSocket("/carSessionChannel/${testCar.vuid}?entity=car") {
                assertEquals(
                    this.closeReason.await(),
                    CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "${testCar.vuid} not registered"),
                )
            }
        }
    }

    /**
     * Test WebSocket can't connect due to invalid query parameters
     *
     * Test that car attempts to connect to the websocket using invalid query parameters
     * and connection is refused
     *
     */
    @Test
    fun testWebSocketInvalidQueryParameters() {
        testApplication {
            application {
                module()
            }

            val carClient =
                createClient {
                    install(WebSockets)
                }

            assertEquals(registerCar(carClient), HttpStatusCode.OK)
            carClient.webSocket("/carSessionChannel/${testCar.vuid}") {
                assertEquals(
                    this.closeReason.await(),
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "An entity 'car' or 'user' must be provided",
                    ),
                )
            }
        }
    }

    /**
     * Test WebSocket can't connect due to invalid query parameters
     *
     * Test that car attempts to connect to the websocket using invalid query parameter values
     * and connection is refused
     *
     */
    @Test
    fun testWebSocketInvalidQueryParameterValue() {
        testApplication {
            application {
                module()
            }

            val carClient =
                createClient {
                    install(WebSockets)
                }

            assertEquals(registerCar(carClient), HttpStatusCode.OK)
            carClient.webSocket("/carSessionChannel/${testCar.vuid}?entity=notcar") {
                assertEquals(
                    this.closeReason.await(),
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "Invalid entity 'notcar'",
                    ),
                )
            }
        }
    }

    /**
     * Test WebSocket message not received with invalid receiver
     *
     * Test that car connects to the websocket using valid parameters and can send a message with
     * an invalid receiver, but can't receive it
     *
     */
    @Test
    fun testWebSocketMessageNotReceivedInvalidReceiver() {
        testApplication {
            application {
                module()
            }

            val carClient =
                createClient {
                    install(WebSockets)
                }

            assertEquals(registerCar(carClient), HttpStatusCode.OK)
            carClient.webSocket("/carSessionChannel/${testCar.vuid}?entity=car") {
                send(Frame.Text(Json.encodeToString(WebSocketMessage("notcar", "hello"))))
                assertNull(incoming.tryReceive().getOrNull())
            }
        }
    }
}
