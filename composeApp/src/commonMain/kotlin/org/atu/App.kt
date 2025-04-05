package org.atu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Available Cars:")
            CarCard(Car(carId = "TestCar 1", fuel = 50.0F, availability = CarAvailability.Available))
            CarCard(Car(carId = "TestCar 2", fuel = 85.0F, availability = CarAvailability.Unavailable))
            CarCard(Car(carId = "TestCar 3", fuel = 32.0F, availability = CarAvailability.Unavailable))
        }
    }
}