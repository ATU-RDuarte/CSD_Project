package org.atu

import kotlin.random.Random.Default.nextFloat
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun carBuilder(
    vuid: String = Uuid.random().toString(),
    carAvailability: CarAvailability = CarAvailability.Available,
    fuel: Float = (100f - (nextFloat() * 100)),
    price: Float = (100f - (nextFloat() * 100)),
): Car =
    Car(
        vuid,
        carAvailability,
        fuel,
        price,
    )
