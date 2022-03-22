package com.kemsky.dipaypokedex.ui.fragment.allpokemon.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kemsky.dipaypokedex.data.model.PokemonAllModel
import com.kemsky.dipaypokedex.data.repository.PokeRepository

class AllPokemonDataSource(
    private val repository: PokeRepository
) : PagingSource<Int, PokemonAllModel.Result>() {
    override fun getRefreshKey(state: PagingState<Int, PokemonAllModel.Result>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonAllModel.Result> {
        try {
            val currentLoadingPageKey = params.key ?: 0
            val response = repository.getAllPokemon(limit = 25, offset = currentLoadingPageKey)
            val responseData = mutableListOf<PokemonAllModel.Result>()
            response.body()?.let { model ->
                responseData.addAll(model.results)
            }

            val prevKey = if (currentLoadingPageKey == 0) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(25)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}