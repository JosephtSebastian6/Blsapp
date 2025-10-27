package com.example.bls.data.model

import com.squareup.moshi.Json

data class Matricula(
    val identificador: Int,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @field:Json(name = "email_verified") val emailVerified: Boolean,
    @field:Json(name = "numero_identificacion") val numeroIdentificacion: String?,
    val ciudad: String?,
    val rh: String?,
    @field:Json(name = "grupo_sanguineo") val grupoSanguineo: String?,
    @field:Json(name = "ano_nacimiento") val anoNacimiento: Int?,
    val direccion: String?,
    val telefono: String?,
    @field:Json(name = "profile_image_url") val profileImageUrl: String?,
    @field:Json(name = "tipo_usuario") val tipoUsuario: String,
    @field:Json(name = "matricula_activa") val matriculaActiva: Boolean
) {
    val displayName: String
        get() {
            val nombreCompleto = "$nombres $apellidos".trim()
            return if (nombreCompleto.isNotEmpty()) nombreCompleto else username
        }
    
    val estadoMatricula: String
        get() = if (matriculaActiva) "Activa" else "Inactiva"
}
