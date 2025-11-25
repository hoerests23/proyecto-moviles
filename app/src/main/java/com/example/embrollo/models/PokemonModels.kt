package com.example.embrollo.models

// Respuesta principal de la API
data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
)

// Cada item de la lista
data class PokemonResult(
    val name: String,
    val url: String
) {
    // Extraemos el ID de la URL
    val id: Int
        get() {
            return try {
                url.trimEnd('/').substringAfterLast('/').toInt()
            } catch (e: Exception) {
                0
            }
        }

    val imageUrl: String
        get() = "https://cdn.jsdelivr.net/gh/PokeAPI/sprites@master/sprites/pokemon/other/official-artwork/$id.png"
}