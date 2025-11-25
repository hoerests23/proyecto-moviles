package com.example.embrollo.data.network

import com.example.embrollo.models.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun obtenerListaPokemon(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse
}