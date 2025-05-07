package org.atu.viewModel

import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.atu.Car
import org.atu.http.AppClientHttpClient

/**
 * Client Application View Model
 *
 * This class handles client application state
 *
 * @param serverUrl server url (differs for android client)
 *
 */
class ClientApplicationViewModel(serverUrl: String) : ViewModel() {
    val httpClient = AppClientHttpClient(serverUrl, HttpClient().engine)
    private val _carListState = MutableStateFlow(listOf<Car>())
    val carListState = _carListState.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                _carListState.value = httpClient.fetchForAvailableCars()
                delay(500)
            }

        }
    }
}