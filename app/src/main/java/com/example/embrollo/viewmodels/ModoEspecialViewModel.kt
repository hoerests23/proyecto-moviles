package com.example.embrollo.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

class ModoEspecialViewModel : ViewModel() {

    private val _activado = MutableStateFlow(false)
    val activado: StateFlow<Boolean> = _activado

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    private val KEY_ACTIVADO = booleanPreferencesKey("modo_activado")

    fun cargarEstado(context: Context) {
        viewModelScope.launch {
            context.dataStore.data.collect { preferences ->
                _activado.value = preferences[KEY_ACTIVADO] ?: false
                _cargando.value = false
            }
        }
    }

    fun toggleActivado(context: Context) {
        viewModelScope.launch {
            val nuevoEstado = !_activado.value
            context.dataStore.edit { preferences ->
                preferences[KEY_ACTIVADO] = nuevoEstado
            }
            _activado.value = nuevoEstado
            _mostrarMensaje.value = true
        }
    }

    fun ocultarMensaje() {
        _mostrarMensaje.value = false
    }
}