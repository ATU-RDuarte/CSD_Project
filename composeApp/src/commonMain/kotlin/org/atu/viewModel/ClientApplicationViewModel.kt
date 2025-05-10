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

    private val _carSessionStatus = MutableStateFlow(false)
    val carSessionStatus = _carSessionStatus.asStateFlow()

    fun setCarSessionStatus(isSessionActive: Boolean) {
        _carSessionStatus.value = isSessionActive
    }

    private val _lastCarStatus = MutableStateFlow("")
    val lastCarStatus = _lastCarStatus.asStateFlow()

    fun setLastCarStatus(status: String) {
        _lastCarStatus.value = status
    }

    init {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                _carListState.value = httpClient.fetchForAvailableCars()
                delay(500)
            }

        }
    }
}