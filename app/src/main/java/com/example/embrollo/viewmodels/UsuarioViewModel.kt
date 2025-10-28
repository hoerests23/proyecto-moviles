package com.example.embrollo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.embrollo.data.database.AppDatabase
import com.example.embrollo.data.entities.UsuarioEntity
import com.example.embrollo.data.repository.UsuarioRepository
import com.example.embrollo.models.GeneroFavorito
import com.example.embrollo.models.UsuarioErrores
import com.example.embrollo.models.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    // Repository
    private val repository: UsuarioRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UsuarioRepository(database.usuarioDao())
    }

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre= valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    fun onConfirmarClaveChange(valor: String) {
        _estado.update {
            it.copy(
                confirmarClave = valor,
                errores = it.errores.copy(confirmarClave = null)
            )
        }
    }

    fun onTelefonoChange(valor: String) {
        if (valor.all { it.isDigit() } || valor.isEmpty()) {
            _estado.update {
                it.copy(
                    telefono = valor,
                    errores = it.errores.copy(telefono = null)
                )
            }
        }
    }

    fun onGeneroToggle(genero: GeneroFavorito) {
        _estado.update { estado ->
            val nuevosGeneros = if (genero in estado.generosFavoritos) {
                estado.generosFavoritos - genero
            } else {
                estado.generosFavoritos + genero
            }
            estado.copy(
                generosFavoritos = nuevosGeneros,
                errores = estado.errores.copy(generosFavoritos = null)
            )
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun toggleMostrarClave() {
        _estado.update { it.copy(mostrarClave = !it.mostrarClave) }
    }

    fun toggleMostrarConfirmarClave() {
        _estado.update { it.copy(mostrarConfirmarClave = !it.mostrarConfirmarClave) }
    }

    fun onFotoPerfilChange(uri: String?) {
        _estado.update {
            it.copy(fotoPerfilUri = uri)
        }
    }
    //validaciones
    private fun validarNombreCompleto(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es obligatorio"
            !nombre.all { it.isLetter() || it.isWhitespace() } ->
                "Solo se permiten letras y espacios"
            nombre.length > 100 ->
                "Máximo 100 caracteres (actual: ${nombre.length})"
            else -> null
        }
    }

    private fun validarCorreo(correo: String): String? {
        val emailRegex = "^[A-Za-z0-9+_.-]+@duoc\\.cl$".toRegex()
        return when {
            correo.isBlank() -> "El correo es obligatorio"
            !correo.matches(emailRegex) -> "Debe ser un correo @duoc.cl válido"
            correo.length > 60 ->
                "Máximo 60 caracteres (actual: ${correo.length})"
            else -> null
        }
    }

    private fun validarClave(clave: String): String? {
        return when {
            clave.length < 10 ->
                "Mínimo 10 caracteres (actual: ${clave.length})"
            !clave.any { it.isUpperCase() } ->
                "Debe contener al menos una mayúscula"
            !clave.any { it.isLowerCase() } ->
                "Debe contener al menos una minúscula"
            !clave.any { it.isDigit() } ->
                "Debe contener al menos un número"
            !clave.any { it in "@#$%!&*-_" } ->
                "Debe contener un carácter especial (@#$%!&*)"
            else -> null
        }
    }

    private fun validarConfirmarClave(clave: String, confirmar: String): String? {
        return when {
            confirmar.isBlank() -> "Debes confirmar la contraseña"
            clave != confirmar -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    private fun validarTelefono(telefono: String): String? {
        if (telefono.isEmpty()) return null

        return when {
            telefono.length < 8 -> "Teléfono inválido (mínimo 8 dígitos)"
            telefono.length > 15 -> "Teléfono inválido (máximo 15 dígitos)"
            else -> null
        }
    }

    private fun validarGenerosFavoritos(generos: Set<GeneroFavorito>): String? {
        return if (generos.isEmpty()) {
            "Debes seleccionar al menos un género favorito"
        } else null
    }
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val errores = UsuarioErrores(
            nombre = validarNombreCompleto(estadoActual.nombre),
            correo = validarCorreo(estadoActual.correo),
            clave = validarClave(estadoActual.clave),
            confirmarClave = validarConfirmarClave(
                estadoActual.clave,
                estadoActual.confirmarClave
            ),
            telefono = validarTelefono(estadoActual.telefono),
            generosFavoritos = validarGenerosFavoritos(estadoActual.generosFavoritos)
        )

        _estado.update { it.copy(errores = errores) }

        return listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.confirmarClave,
            errores.telefono,
            errores.generosFavoritos
        ).isEmpty()
    }

    fun registrarUsuario(onExito: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Verificar si el correo ya existe
                if (repository.existeCorreo(_estado.value.correo)) {
                    onError("Este correo ya está registrado")
                    return@launch
                }

                // Registrar usuario
                val resultado = repository.registrarUsuario(_estado.value)

                if (resultado > 0) {
                    _registroExitoso.value = true
                    onExito()
                } else {
                    onError("Error al registrar usuario. Intenta nuevamente.")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun obtenerUsuarioRegistrado(): UsuarioEntity? {
        val estadoActual = _estado.value
        return UsuarioEntity(
            correo = estadoActual.correo,
            nombre = estadoActual.nombre,
            clave = estadoActual.clave,
            telefono = estadoActual.telefono,
            generosFavoritos = estadoActual.generosFavoritos.joinToString(",") { it.name },
            fotoPerfilUri = estadoActual.fotoPerfilUri,
            fechaRegistro = System.currentTimeMillis()
        )
    }
    fun limpiarFormulario() {
        _estado.value = UsuarioUiState()
        _registroExitoso.value = false
    }
}