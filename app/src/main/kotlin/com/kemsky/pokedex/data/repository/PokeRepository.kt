package com.kemsky.pokedex.data.repository

import com.kemsky.pokedex.data.model.PokemonAllModel
import com.kemsky.pokedex.data.model.PokemonDetailModel
import com.kemsky.pokedex.data.model.PokemonFormModel
import com.kemsky.pokedex.data.model.PokemonSpeciesModel
import retrofit2.Response

interface PokeRepository {

    suspend fun getAllPokemon(limit: Int, offset: Int): Response<PokemonAllModel>
    suspend fun getDetailPokemon(pokemonId: Int): Response<PokemonDetailModel>
    suspend fun getFormPokemon(pokemonId: Int): Response<PokemonFormModel>
    suspend fun getSpeciesPokemon(pokemonId: Int): Response<PokemonSpeciesModel>

}