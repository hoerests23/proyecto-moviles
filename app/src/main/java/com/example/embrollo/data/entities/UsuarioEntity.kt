package com.example.embrollo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val correo: String,
    val clave: String,
    val telefono: String,
    val generosFavoritos: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)