package org.atu.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.atu.Car
import org.atu.CarAvailability
import org.atu.carBuilder

@Serializable
object CarList
@Serializable
data class CarView(val vuid: String)

@Composable
fun ClientApp(navController: NavHostController = rememberNavController()){
    //TODO: Get cars from server's database
    val carList = listOf(
        carBuilder(),
        carBuilder(carAvailability = CarAvailability.Unavailable),
        carBuilder(fuel = 85.0F, carAvailability = CarAvailability.Unavailable),
    )
    Scaffold{
        innerPadding -> NavHost(
            navController = navController,
            startDestination = CarList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            composable<CarList> {
                CarListScreen(carList) { car -> navController.navigate(CarView(car)) }
            }
            composable<CarView>{
                val carVuid : String = it.toRoute<CarView>().vuid
                val car : Car = carList.find { car -> car.vuid == carVuid }!!
                CarViewScreen(car) { navController.navigateUp() }
            }
        }
    }
}