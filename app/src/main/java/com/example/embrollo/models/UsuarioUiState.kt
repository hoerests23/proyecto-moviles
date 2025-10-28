package com.example.embrollo.models

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val telefono: String = "",
    //val direccion: String = "",
    val mostrarClave: Boolean = false,
    val mostrarConfirmarClave: Boolean = false,

    val fotoPerfilUri: String? = null,
    val generosFavoritos: Set<GeneroFavorito> = emptySet(),

    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()

)
    enum class GeneroFavorito(val displayName: String){
        SUSPENSO("Suspenso"),
        ACCION("Acción"),
        MISTERIO("Misterio"),
        FICCION("Ficción"),
        TERROR("Terror"),
        HISTORIA("Historia")
    }
