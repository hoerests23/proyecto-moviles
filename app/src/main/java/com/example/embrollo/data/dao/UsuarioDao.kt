package com.example.embrollo.data.dao

import androidx.room.*
import com.example.embrollo.data.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    //usuario correo
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?

    //verificar correo existente
    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE correo = :correo)")
    suspend fun existeCorreo(correo: String): Boolean

    //obtener todos los usuarios
    @Query("SELECT * FROM usuarios ORDER BY fechaRegistro DESC")
    fun obtenerTodosLosUsuarios(): Flow<List<UsuarioEntity>>

    //verificar credenciales
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun verificarCredenciales(correo: String, clave: String): UsuarioEntity?
    //eliminar
    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)

    //limpiar tabla
    @Query("DELETE FROM usuarios")
    suspend fun limpiarTabla()
}