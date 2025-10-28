package com.example.embrollo.data.repository

import com.example.embrollo.data.dao.UsuarioDao
import com.example.embrollo.data.entities.UsuarioEntity
import com.example.embrollo.models.GeneroFavorito
import com.example.embrollo.models.UsuarioUiState
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun registrarUsuario(estado: UsuarioUiState): Long {
        return try {
            // Verificar si el correo ya existe
            if (usuarioDao.existeCorreo(estado.correo)) {
                return -1L // Correo duplicado
            }

            val entity = UsuarioEntity(
                nombre = estado.nombre,
                correo = estado.correo,
                clave = estado.clave,
                telefono = estado.telefono,
                generosFavoritos = generosToJson(estado.generosFavoritos),
                fotoPerfilUri = estado.fotoPerfilUri
            )

            usuarioDao.insertarUsuario(entity)
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    suspend fun verificarLogin(correo: String, clave: String): UsuarioEntity? {
        return usuarioDao.verificarCredenciales(correo, clave)
    }

    suspend fun existeCorreo(correo: String): Boolean {
        return usuarioDao.existeCorreo(correo)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>> {
        return usuarioDao.obtenerTodosLosUsuarios()
    }

    private fun generosToJson(generos: Set<GeneroFavorito>): String {
        return generos.joinToString(",") { it.name }
    }


    suspend fun obtenerUltimoUsuario(): UsuarioEntity? {
        return usuarioDao.obtenerUltimoUsuario()
    }

    //pasar los generos en string
    fun jsonToGeneros(json: String): Set<GeneroFavorito> {
        if (json.isBlank()) return emptySet()
        return json.split(",")
            .mapNotNull {
                try {
                    GeneroFavorito.valueOf(it)
                } catch (e: Exception) {
                    null
                }
            }
            .toSet()
    }
}