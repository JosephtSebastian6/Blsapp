package com.example.bls.data.model

import com.squareup.moshi.Json

data class Unidad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    @field:Json(name = "subcarpetas_count") val subcarpetasCount: Int,
    val habilitada: Boolean = true
)

data class Subcarpeta(
    val id: Int,
    @field:Json(name = "unidad_id") val unidadId: Int,
    val nombre: String,
    val descripcion: String? = null,
    val habilitada: Boolean,
    val orden: Int
)
