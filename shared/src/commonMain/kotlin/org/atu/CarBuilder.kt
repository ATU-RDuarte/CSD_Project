package org.atu

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun carBuilder(
    vuid: String = Uuid.random().toString(),
    carAvailability: CarAvailability = CarAvailability.Available,
    fuel: Float = 100f
): Car = Car(
    vuid,
    carAvailability,
    fuel
)