package com.kemsky.pokedex.domain.usecase.all

import com.kemsky.pokedex.data.model.PokemonAllModel
import retrofit2.Response

interface IPokemonAllRepository {
    suspend fun getAllPokemon(limit: Int, offset: Int): Response<PokemonAllModel>
}