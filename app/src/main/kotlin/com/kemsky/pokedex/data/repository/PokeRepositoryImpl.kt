package com.kemsky.pokedex.data.repository

import com.kemsky.pokedex.data.model.PokemonAllModel
import com.kemsky.pokedex.data.model.PokemonDetailModel
import com.kemsky.pokedex.data.model.PokemonFormModel
import com.kemsky.pokedex.data.model.PokemonSpeciesModel
import com.kemsky.pokedex.data.remote.ApiService
import retrofit2.Response


class PokeRepositoryImpl(private val apiService: ApiService) : PokeRepository {

    override suspend fun getAllPokemon(limit: Int, offset: Int): Response<PokemonAllModel> {
        return apiService.getAllPokemon(limit, offset)
    }

    override suspend fun getDetailPokemon(pokemonId: Int): Response<PokemonDetailModel> {
        return apiService.getDetailPokemon(pokemonId)
    }

    override suspend fun getFormPokemon(pokemonId: Int): Response<PokemonFormModel> {
        return apiService.getFormPokemon(pokemonId)
    }

    override suspend fun getSpeciesPokemon(pokemonId: Int): Response<PokemonSpeciesModel> {
        return apiService.getSpeciesPokemon(pokemonId)
    }

}