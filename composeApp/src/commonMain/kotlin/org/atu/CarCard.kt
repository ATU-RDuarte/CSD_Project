package org.atu

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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CarCard(car: Car) {
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
                .height(100.dp),
    ) {
        Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.End) {
            Row(
                Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Id: ${car.vuid}")
                Icon(
                    imageVector = batteryIconFromCarFuel.first,
                    contentDescription = "Fuel",
                    tint = batteryIconFromCarFuel.second,
                )
            }
            Text("Availability: ${car.availability}")
        }
    }
}
