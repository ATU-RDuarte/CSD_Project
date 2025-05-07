package org.atu

import kotlinx.serialization.json.Json
import kotlin.random.Random.Default.nextFloat
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Car builder method
 *
 * Builds a car data object from parameters or defaults values
 *
 * @param vuid car vuid
 * @param carAvailability car availability status
 * @param fuel car fuel percentage
 * @param price car price per hour
 *
 */
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

/**
 * Car Json Serializer
 *
 * Builds JSON string from car data object
 *
 * @param car car to be serialized
 *
 */
fun carJsonSerializer(car: Car) = Json.encodeToString(car)

/**
 * Json Car Parser
 *
 * Parses JSON string to car data object
 *
 * @param carJsonData JSON string to be parsed
 *
 */
fun jsonCarParser(carJsonData: String) = Json.decodeFromString<Car>(carJsonData)
