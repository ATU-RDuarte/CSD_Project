package org.atu

import kotlinx.serialization.Serializable

@Serializable
data class Car(val vuid: String, var availability: CarAvailability, var fuel: Float, var price: Float)
