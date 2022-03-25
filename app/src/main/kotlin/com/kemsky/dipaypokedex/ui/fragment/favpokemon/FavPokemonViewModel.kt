package com.kemsky.dipaypokedex.ui.fragment.favpokemon

import androidx.lifecycle.ViewModel
import com.kemsky.dipaypokedex.data.room.PokemonDatabase
import com.kemsky.dipaypokedex.data.room.model.FavPokemonModel
import kotlinx.coroutines.flow.Flow

class FavPokemonViewModel(
    private val database: PokemonDatabase
) : ViewModel() {

    fun getListFav(): Flow<List<FavPokemonModel>> {
        val dao = database.favPokemonDao()
        return dao.getAllFav()
    }

}