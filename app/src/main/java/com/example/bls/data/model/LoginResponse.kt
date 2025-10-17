package com.example.bls.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("tipo_usuario") val userType: String,
    val usuario: Usuario
)

data class Usuario(
    val identificador: Int,
    val username: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    @SerializedName("email_verified") val emailVerified: Boolean,
    @SerializedName("numero_identificacion") val numeroIdentificacion: String?,
    val ciudad: String?,
    val rh: String?,
    @SerializedName("grupo_sanguineo") val grupoSanguineo: String?,
    @SerializedName("ano_nacimiento") val anoNacimiento: String?,
    val direccion: String?,
    val telefono: String?,
    @SerializedName("profile_image_url") val profileImageUrl: String?,
    @SerializedName("tipo_usuario") val tipoUsuario: String,
    @SerializedName("matricula_activa") val matriculaActiva: Boolean
)
