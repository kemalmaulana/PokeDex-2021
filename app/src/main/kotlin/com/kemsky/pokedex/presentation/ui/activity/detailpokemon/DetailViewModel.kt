package com.kemsky.pokedex.presentation.ui.activity.detailpokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kemsky.pokedex.core.helper.Resource
import com.kemsky.pokedex.data.model.PokemonDetailModel
import com.kemsky.pokedex.data.model.PokemonSpeciesModel
import com.kemsky.pokedex.data.room.PokemonDatabase
import com.kemsky.pokedex.data.room.model.FavPokemonModel
import com.kemsky.pokedex.domain.usecase.detail.IPokemonDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IPokemonDetailRepository,
    private val database: PokemonDatabase
) : ViewModel() {

    suspend fun fetchDetailPokemon(pokeId: Int): Flow<Resource<PokemonDetailModel?>> = flow {
        emit(Resource.Loading())
        try {
            val response = repository.getDetailPokemon(pokeId)
            if (response.code() == 200 && response.body() != null) {
                response.body()?.let { model ->
                    emit(Resource.Success(model))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (ioe: IOException) {
            emit(Resource.Error(ioe.localizedMessage))
        }
    }

    suspend fun fetchSpeciesPokemon(pokeId: Int): Flow<Resource<PokemonSpeciesModel>> = flow {
        try {
            val response = repository.getSpeciesPokemon(pokeId)
            if (response.code() == 200 && response.body() != null) {
                response.body()?.let { model ->
                    emit(Resource.Success(model))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (ioe: IOException) {
            emit(Resource.Error(ioe.localizedMessage))
        }
    }

    suspend fun addToFavorite(pokemon: FavPokemonModel) {
        if (database.openHelper.writableDatabase.isOpen) {
            Timber.e("Inserting ${pokemon.name}")
            val dao = database.favPokemonDao()
            viewModelScope.launch(Dispatchers.IO) {
            dao.insertSingle(pokemon)
            }
        }
    }

    fun deleteFromFavorite(pokemon: FavPokemonModel) {
        if (database.openHelper.writableDatabase.isOpen) {
            val dao = database.favPokemonDao()
            viewModelScope.launch {
                dao.delete(pokemon)
            }
        }
    }

    fun getSingleFav(id: Int?): Flow<FavPokemonModel?>? {
        val dao = database.favPokemonDao()
        return id?.let { dao.getSingleFav(it) }
    }
}