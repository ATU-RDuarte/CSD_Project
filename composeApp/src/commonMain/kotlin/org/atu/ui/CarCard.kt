package org.atu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Battery1Bar
import androidx.compose.material.icons.outlined.Battery4Bar
import androidx.compose.material.icons.outlined.BatteryFull
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.atu.Car
import org.atu.CarAvailability
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Car Card class for UI module
 *
 * This class is a UI module that generates a card for a card list with car information
 *
 * @param car current car on the card
 * @param onCardClick callback that runs on card click
 *
 */
@Preview
@Composable
fun CarCard(car: Car, onCardClick: (String) -> Unit) {
    val batteryIconFromCarFuel: Pair<ImageVector, Color> =
        when (car.fuel) {
            in 0.0F..35F -> Pair(Icons.Outlined.Battery1Bar, Color.Red)
            in 35.1F..70.0F -> Pair(Icons.Outlined.Battery4Bar, Color.Yellow)
            else -> Pair(Icons.Outlined.BatteryFull, Color.Green)
        }
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(100.dp)
                .clickable(car.availability == CarAvailability.Available, onClick =  { onCardClick(car.vuid) }),
    ) {
        Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.End) {
            Row(
                Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Id: ${car.vuid}")
            }
            Row(
                Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    imageVector = batteryIconFromCarFuel.first,
                    contentDescription = "Fuel",
                    tint = batteryIconFromCarFuel.second,
                )
                Text("Availability: ${car.availability}")
            }
        }
    }
}
