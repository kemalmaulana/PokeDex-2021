package com.kemsky.pokedex.domain.usecase.all

import com.kemsky.pokedex.data.model.PokemonAllModel
import com.kemsky.pokedex.data.repository.PokeRepository
import retrofit2.Response
import javax.inject.Inject

class PokemonAllRepositoryImpl @Inject constructor(
    private val repository: PokeRepository
) : IPokemonAllRepository {

    override suspend fun getAllPokemon(limit: Int, offset: Int): Response<PokemonAllModel> {
        return repository.getAllPokemon(limit, offset)
    }
}