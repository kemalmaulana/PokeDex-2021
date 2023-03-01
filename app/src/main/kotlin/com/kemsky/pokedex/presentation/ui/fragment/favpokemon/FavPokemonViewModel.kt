package com.kemsky.pokedex.presentation.ui.fragment.favpokemon

import androidx.lifecycle.ViewModel
import com.kemsky.pokedex.data.room.PokemonDatabase
import com.kemsky.pokedex.data.room.model.FavPokemonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavPokemonViewModel @Inject constructor(
    private val database: PokemonDatabase
) : ViewModel() {

    fun getListFav(): Flow<List<FavPokemonModel>> {
        val dao = database.favPokemonDao()
        return dao.getAllFav()
    }

}