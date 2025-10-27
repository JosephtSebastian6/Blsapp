package com.example.bls.data.model

import com.squareup.moshi.Json

data class Profesor(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @field:Json(name = "tipo_usuario") val tipoUsuario: String,
    @field:Json(name = "profile_image_url") val profileImageUrl: String? = null
) {
    val displayName: String
        get() {
            val nombreCompleto = "$nombres $apellidos".trim()
            return if (nombreCompleto.isNotEmpty()) nombreCompleto else username
        }
    
    val handle: String get() = "@$username"
}

data class ProfesorResumen(
    @field:Json(name = "profesor_username") val profesorUsername: String,
    @field:Json(name = "grupos_creados") val gruposCreados: Int,
    @field:Json(name = "estudiantes_asignados") val estudiantesAsignados: Int
)
