package org.atu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.atu.Car
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CarListScreen(carList : List<Car>, carCardOnClickAction: (String) -> Unit){
    Scaffold {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Existing Cars:")
            for(car in carList)
            {
                CarCard(car, carCardOnClickAction)
            }
        }
    }
}