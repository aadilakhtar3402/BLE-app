package com.example.locationapp.data

data class SensorResult(
    val temperature: Float,
    val humidity: Float,
    val connectionSate: ConnectionState
)
