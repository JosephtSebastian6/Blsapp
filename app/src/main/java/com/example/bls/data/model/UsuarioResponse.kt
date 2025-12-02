package com.example.bls.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsuarioResponse(
    val identificador: Int? = null,
    val username: String,
    val nombres: String?,
    val apellidos: String?,
    val email: String,
    @field:Json(name = "email_verified") val emailVerified: Boolean? = null,
    @field:Json(name = "numero_identificacion") val numeroIdentificacion: String?,
    val ciudad: String?,
    val rh: String? = null,
    @field:Json(name = "grupo_sanguineo") val grupoSanguineo: String? = null,
    @field:Json(name = "ano_nacimiento") val anoNacimiento: String?, // Puede venir como Int o String, Moshi puede manejarlo si se configura, pero String es mas seguro si el JSON varía
    val direccion: String?,
    val telefono: String?,
    @field:Json(name = "profile_image_url") val profileImageUrl: String?,
    @field:Json(name = "tipo_usuario") val tipoUsuario: String?,
    @field:Json(name = "matricula_activa") val matriculaActiva: Boolean? = null
)
