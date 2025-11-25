package com.example.embrollo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embrollo.data.network.RetrofitPokemonClient
import com.example.embrollo.models.PokemonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    private val _listaPokemon = MutableStateFlow<List<PokemonResult>>(emptyList())
    val listaPokemon: StateFlow<List<PokemonResult>> = _listaPokemon.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarPokemon()
    }

    fun cargarPokemon() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                val respuesta = RetrofitPokemonClient.apiService.obtenerListaPokemon(limit = 50)
                _listaPokemon.value = respuesta.results
            } catch (e: Exception) {
                _error.value = "Error al cargar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }
}