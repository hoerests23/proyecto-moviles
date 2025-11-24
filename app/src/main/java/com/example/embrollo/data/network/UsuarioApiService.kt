package com.example.embrollo.data.network

import com.example.embrollo.data.entities.UsuarioEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioApiService {

    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioEntity>

    @POST("/api/usuarios/registro")
    suspend fun registrarUsuario(@Body usuario: UsuarioEntity): Response<UsuarioEntity>


    data class LoginRequest(val correo: String, val clave: String)

    @POST("/api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioEntity>
}