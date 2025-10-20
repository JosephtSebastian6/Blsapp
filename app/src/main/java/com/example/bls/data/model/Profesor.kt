package com.example.bls.data.model

import com.google.gson.annotations.SerializedName

data class Profesor(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @SerializedName("tipo_usuario") val tipoUsuario: String,
    @SerializedName("profile_image_url") val profileImageUrl: String? = null
) {
    val displayName: String
        get() {
            val nombreCompleto = "$nombres $apellidos".trim()
            return if (nombreCompleto.isNotEmpty()) nombreCompleto else username
        }
    
    val handle: String get() = "@$username"
}

data class ProfesorResumen(
    @SerializedName("profesor_username") val profesorUsername: String,
    @SerializedName("grupos_creados") val gruposCreados: Int,
    @SerializedName("estudiantes_asignados") val estudiantesAsignados: Int
)
