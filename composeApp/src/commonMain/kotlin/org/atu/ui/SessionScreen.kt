package org.atu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.atu.RsaKeyHelper.base64ToBytes
import org.atu.createSelfSignedJwt
import org.atu.viewModel.ClientApplicationViewModel
import org.atu.websockets.UserSocketSession
import org.atu.websockets.WebSocketMessage
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

/***
 * Function responsible to send request to car
 *
 * @param socket current session socket
 * @param message message to be sent in request
 *
 */
suspend fun sendRequestToCar(socket: UserSocketSession, message: WebSocketMessage) {
    socket.sendMessage(
        Json.encodeToString(
            message
        )
    )
}

/***
 * Function responsible to receive car response on request
 *
 * @param socket current session socket
 *
 */
suspend fun receiveResponse(socket: UserSocketSession): String {
    val serializedMessage = socket.receiveMessage() ?: return ""
    val parsedMessage = Json.decodeFromString<WebSocketMessage>(serializedMessage)
    return if ("CAR_LOCKED" == parsedMessage.payload || "CAR_UNLOCKED" == parsedMessage.payload) {
        parsedMessage.payload
    } else {
        ""
    }
}

/**
 * Car SessionScreen for UI module
 *
 * This class is a UI module that shows current session screen
 *
 * @param carVuid current car vuid in session
 * @param viewModel application main view model
 * @param endSessionAction callback on end session
 *
 */
@Preview
@Composable
fun SessionScreen(
    carVuid: String,
    viewModel: ClientApplicationViewModel,
    endSessionAction: () -> Unit
) {
    val carSessionStatus by viewModel.carSessionStatus.collectAsState()
    val lastCarStatus by viewModel.lastCarStatus.collectAsState()
    val socket = UserSocketSession(viewModel.httpClient.getHttpClient())
    // TODO replace with car key instead
    var carPrivateCar: RSAPrivateKey? = null
    CoroutineScope(Dispatchers.Main).launch {
        val base64EncodedCarPrivateKey = viewModel.httpClient.requestCarSession(carVuid)
        println("Obtained JWT: $base64EncodedCarPrivateKey")
        carPrivateCar = KeyFactory.getInstance("RSA").generatePrivate(
            PKCS8EncodedKeySpec(
                base64ToBytes(base64EncodedCarPrivateKey)
            )
        ) as RSAPrivateKey
    }
    CoroutineScope(Dispatchers.Main).launch {
        val url = "${viewModel.httpClient.getWebSocketUrl()}/carSessionChannel/$carVuid?entity=user"
        println("Attempting to connect to $url")
        while (!carSessionStatus) {
            println("Session not initialized")
            delay(200)
            viewModel.setCarSessionStatus(socket.initSocket(url))
        }
    }
    Scaffold {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Session with car $carVuid",
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(20.dp)
            )
            Row(
                Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Connection Status:")
                Icon(
                    imageVector = if (carSessionStatus) {
                        Icons.Filled.Wifi
                    } else {
                        Icons.Filled.WifiOff
                    },
                    contentDescription = "Car connection status",
                )
            }
            Column(
                Modifier.fillMaxWidth().padding(75.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            sendRequestToCar(
                                socket,
                                WebSocketMessage(
                                    "car",
                                    createSelfSignedJwt(
                                        """{"request": "UNLOCK_CAR"}""", carPrivateCar!!
                                    )
                                )
                            )
                            viewModel.setLastCarStatus(receiveResponse(socket))
                        }
                    }
                ) { Text("Request Car Unlock", fontWeight = FontWeight.W800) }
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            sendRequestToCar(
                                socket,
                                WebSocketMessage(
                                    "car",
                                    createSelfSignedJwt(
                                        """{"request": "LOCK_CAR"}""",
                                        carPrivateCar!!
                                    )
                                )
                            )
                            viewModel.setLastCarStatus(receiveResponse(socket))
                        }
                    }
                ) { Text("Request Car Lock", fontWeight = FontWeight.W800) }
                Row(
                    Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text("Server Response: $lastCarStatus")
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch { socket.closeSession() }
                        viewModel.setCarSessionStatus(false)
                        endSessionAction()
                    }
                ) { Text("End Session", fontWeight = FontWeight.W800) }
            }
        }
    }
}