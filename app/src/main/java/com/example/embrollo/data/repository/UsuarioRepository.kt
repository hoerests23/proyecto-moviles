package com.example.embrollo.data.repository

import com.example.embrollo.data.dao.UsuarioDao
import com.example.embrollo.data.entities.UsuarioEntity
import com.example.embrollo.data.network.RetrofitClient
import com.example.embrollo.models.GeneroFavorito
import com.example.embrollo.models.UsuarioUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    // CAMBIO PRINCIPAL: Ahora retorna Int o Long basado en el código HTTP o ID
    suspend fun registrarUsuario(estado: UsuarioUiState): Long {
        return try {
            val entity = UsuarioEntity(
                nombre = estado.nombre,
                correo = estado.correo,
                clave = estado.clave,
                telefono = estado.telefono,
                generosFavoritos = generosToJson(estado.generosFavoritos),
                fotoPerfilUri = estado.fotoPerfilUri
            )

            // --- LLAMADA A LA API ---
            val response = RetrofitClient.apiService.registrarUsuario(entity)

            if (response.isSuccessful && response.body() != null) {
                // Opcional: Guardar copia local en Room para que funcione sin internet
                // usuarioDao.insertarUsuario(entity)

                return response.body()?.id?.toLong() ?: 1L
            } else {

                return -1L
            }
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    suspend fun verificarLogin(correo: String, clave: String): UsuarioEntity? {
        return try {
            val request = com.example.embrollo.data.network.UsuarioApiService.LoginRequest(correo, clave)
            val response = RetrofitClient.apiService.login(request)

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Funciones auxiliares se mantienen igual
    private fun generosToJson(generos: Set<GeneroFavorito>): String {
        return generos.joinToString(",") { it.name }
    }


    suspend fun existeCorreo(correo: String): Boolean {

        return false
    }
}