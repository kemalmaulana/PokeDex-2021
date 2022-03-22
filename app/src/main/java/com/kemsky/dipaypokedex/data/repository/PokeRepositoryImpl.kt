package com.kemsky.dipaypokedex.data.repository

import com.kemsky.dipaypokedex.data.model.PokemonAllModel
import com.kemsky.dipaypokedex.data.model.PokemonDetailModel
import com.kemsky.dipaypokedex.data.model.PokemonFormModel
import com.kemsky.dipaypokedex.data.model.PokemonSpeciesModel
import com.kemsky.dipaypokedex.data.remote.ApiService
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