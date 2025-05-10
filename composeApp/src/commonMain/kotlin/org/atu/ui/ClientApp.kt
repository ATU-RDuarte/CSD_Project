package org.atu.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.atu.Car
import org.atu.viewModel.ClientApplicationViewModel

/**
 * Data class used for routing to the car list
 *
 * This data class is used as the navigation route to the car list screen
 *
 */
@Serializable
object CarList

/**
 * Data class used for routing to car view
 *
 * This data class is used as the navigation route to the car view screen
 *
 * @param vuid of the selected car
 *
 */
@Serializable
data class CarView(val vuid: String)


@Serializable
data class Session(val vuid: String)

/**
 * ClientApp class for UI module
 *
 * This class is the main app screen tht lists all available cars and handles screen navigation
 *
 * @param navController compose navigation handler
 *
 */
@Composable
fun ClientApp(serverUrl: String, navController: NavHostController = rememberNavController()) {
    //TODO: Get cars from server's database
    val clientApplicationViewModel = ClientApplicationViewModel(serverUrl)
    val carList by clientApplicationViewModel.carListState.collectAsState()
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CarList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            composable<CarList> {
                CarListScreen(clientApplicationViewModel) { car -> navController.navigate(CarView(car)) }
            }
            composable<CarView> {
                val carVuid: String = it.toRoute<CarView>().vuid
                val car: Car = carList.find { car -> car.vuid == carVuid }!!
                CarViewScreen(car, { navController.navigate(Session(carVuid)) }, { navController.navigateUp() })
            }
            composable<Session> {
                val carVuid: String = it.toRoute<Session>().vuid
                SessionScreen(carVuid, clientApplicationViewModel){ navController.navigate(CarList) }
            }
        }
    }
}