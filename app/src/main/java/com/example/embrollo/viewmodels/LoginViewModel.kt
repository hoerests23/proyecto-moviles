package com.example.embrollo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.embrollo.data.database.AppDatabase
import com.example.embrollo.data.entities.UsuarioEntity
import com.example.embrollo.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val recordarSesion: Boolean = false,
    val mostrarClave: Boolean = false,
    val errorMensaje: String? = null,
    val isLoading: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UsuarioRepository(database.usuarioDao())
    }

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    // Usuario autenticado
    private val _usuarioAutenticado = MutableStateFlow<UsuarioEntity?>(null)
    val usuarioAutenticado: StateFlow<UsuarioEntity?> = _usuarioAutenticado.asStateFlow()

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(correo = valor, errorMensaje = null)
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(clave = valor, errorMensaje = null)
        }
    }

    fun onRecordarSesionChange(valor: Boolean) {
        _estado.update { it.copy(recordarSesion = valor) }
    }

    fun toggleMostrarClave() {
        _estado.update { it.copy(mostrarClave = !it.mostrarClave) }
    }

    //validacion
    private fun validarCorreo(correo: String): String? {
        return when {
            correo.isBlank() -> "El correo es obligatorio"
            !correo.endsWith("@duoc.cl") -> "Debe ser un correo @duoc.cl"
            else -> null
        }
    }

    private fun validarClave(clave: String): String? {
        return when {
            clave.isBlank() -> "La contraseña es obligatoria"
            //clave.length < 10 -> "La contraseña debe tener al menos 10 caracteres"
            else -> null
        }
    }

    //login room

    fun intentarLogin(onExito: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val estadoActual = _estado.value

            // Validar campos
            val errorCorreo = validarCorreo(estadoActual.correo)
            val errorClave = validarClave(estadoActual.clave)

            when {
                errorCorreo != null -> {
                    _estado.update { it.copy(errorMensaje = errorCorreo) }
                    onError(errorCorreo)
                }
                errorClave != null -> {
                    _estado.update { it.copy(errorMensaje = errorClave) }
                    onError(errorClave)
                }
                else -> {
                    // Validaciones OK, verificar en BD
                    _estado.update { it.copy(isLoading = true) }

                    try {
                        val usuario = repository.verificarLogin(
                            estadoActual.correo,
                            estadoActual.clave
                        )

                        if (usuario != null) {
                            // Login exitoso
                            _usuarioAutenticado.value = usuario
                            _estado.update {
                                it.copy(isLoading = false, errorMensaje = null)
                            }
                            onExito()
                        } else {
                            // Credenciales incorrectas
                            val mensaje = "Correo o contraseña incorrectos"
                            _estado.update {
                                it.copy(isLoading = false, errorMensaje = mensaje)
                            }
                            onError(mensaje)
                        }
                    } catch (e: Exception) {
                        val mensaje = "Error al iniciar sesión: ${e.message}"
                        _estado.update {
                            it.copy(isLoading = false, errorMensaje = mensaje)
                        }
                        onError(mensaje)
                    }
                }
            }
        }
    }

    //a implementar
    fun cerrarSesion() {
        _usuarioAutenticado.value = null
        _estado.value = LoginUiState()
    }

    fun obtenerUsuarioActual(): UsuarioEntity? {
        return _usuarioAutenticado.value
    }

    fun establecerUsuarioAutenticado(usuario: UsuarioEntity?) {
        _usuarioAutenticado.value = usuario
    }

    fun limpiarError() {
        _estado.update { it.copy(errorMensaje = null) }
    }
}