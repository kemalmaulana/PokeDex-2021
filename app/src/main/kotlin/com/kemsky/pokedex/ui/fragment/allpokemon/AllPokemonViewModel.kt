package com.kemsky.pokedex.ui.fragment.allpokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kemsky.pokedex.data.repository.PokeRepository
import com.kemsky.pokedex.ui.fragment.allpokemon.adapter.AllPokemonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllPokemonViewModel @Inject constructor(private val repository: PokeRepository) : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 9)) {
        AllPokemonDataSource(repository)
    }.flow.cachedIn(viewModelScope)
}