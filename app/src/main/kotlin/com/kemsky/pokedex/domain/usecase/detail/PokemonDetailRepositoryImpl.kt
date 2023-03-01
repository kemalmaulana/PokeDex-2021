package com.kemsky.pokedex.domain.usecase.detail

import com.kemsky.pokedex.data.model.PokemonDetailModel
import com.kemsky.pokedex.data.model.PokemonFormModel
import com.kemsky.pokedex.data.model.PokemonSpeciesModel
import com.kemsky.pokedex.data.repository.PokeRepository
import retrofit2.Response
import javax.inject.Inject

class PokemonDetailRepositoryImpl @Inject constructor(
    val repository: PokeRepository
) : IPokemonDetailRepository {
    override suspend fun getDetailPokemon(pokemonId: Int): Response<PokemonDetailModel> {
        return repository.getDetailPokemon(pokemonId)
    }

    override suspend fun getFormPokemon(pokemonId: Int): Response<PokemonFormModel> {
        return repository.getFormPokemon(pokemonId)
    }

    override suspend fun getSpeciesPokemon(pokemonId: Int): Response<PokemonSpeciesModel> {
        return repository.getSpeciesPokemon(pokemonId)
    }
}