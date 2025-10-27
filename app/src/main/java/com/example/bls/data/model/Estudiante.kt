package com.example.bls.data.model

import com.squareup.moshi.Json

data class EstudianteGestion(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @field:Json(name = "matricula_activa") val matriculaActiva: Boolean = true,
    // Este campo se añadirá en el ViewModel, no viene de la API
    var isAssigned: Boolean = false 
) {
    val displayName: String
        get() {
            val nombreCompleto = "$nombres $apellidos".trim()
            return if (nombreCompleto.isNotEmpty()) nombreCompleto else username
        }
}

data class EstudianteAsignado(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @field:Json(name = "matricula_activa") val matriculaActiva: Boolean = true
)
