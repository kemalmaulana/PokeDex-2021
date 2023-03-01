package com.kemsky.pokedex.domain.usecase.detail

import com.kemsky.pokedex.data.model.PokemonDetailModel
import com.kemsky.pokedex.data.model.PokemonFormModel
import com.kemsky.pokedex.data.model.PokemonSpeciesModel
import retrofit2.Response

interface IPokemonDetailRepository {
    suspend fun getDetailPokemon(pokemonId: Int): Response<PokemonDetailModel>
    suspend fun getFormPokemon(pokemonId: Int): Response<PokemonFormModel>
    suspend fun getSpeciesPokemon(pokemonId: Int): Response<PokemonSpeciesModel>
}