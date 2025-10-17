package com.example.bls.data.model

import com.google.gson.annotations.SerializedName

// Modelo ajustado para que coincida con la respuesta real de la API
data class Unidad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    @SerializedName("subcarpetas_count") val subcarpetasCount: Int
)

// La data class Subcarpeta se puede mantener para un uso futuro, no causa problemas.
data class Subcarpeta(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null
)
