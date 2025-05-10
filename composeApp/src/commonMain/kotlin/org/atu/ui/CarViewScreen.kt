package org.atu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.atu.Car
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * CarViewScree class for UI module
 *
 * This class is a UI that is routed on car card click and shows current car details
 * and provides a button to request a session to the car
 *
 * @param car current selected car
 * @param goBackAction callback to run on back button click action
 *
 */
@Preview
@Composable
fun CarViewScreen(car: Car, onRequestSessionClicked: () -> Unit , goBackAction: () -> Unit) {
    Scaffold {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            GoBackButton(goBackAction)
            Row(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Start,
            ){
                Text("Car ID: ", fontWeight = FontWeight.W800)
                Text(car.vuid)
            }
            Row(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Start,
            ){
                Text("Fuel: ", fontWeight = FontWeight.W800)
                Text(car.fuel.toString())
            }
            Row(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Start,
            ){
                Text("Price: ", fontWeight = FontWeight.W800)
                Text("${car.fuel}€/h")
            }
            Row(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Center,
            ){
                Button(
                    onClick = onRequestSessionClicked
                ){ Text("Request Session", fontWeight = FontWeight.W800)}
            }
        }
    }
}