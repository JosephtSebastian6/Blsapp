package com.example.bls.data.model

import com.google.gson.annotations.SerializedName

data class EstudianteGestion(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @SerializedName("matricula_activa") val matriculaActiva: Boolean = true,
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
    @SerializedName("matricula_activa") val matriculaActiva: Boolean = true
)
