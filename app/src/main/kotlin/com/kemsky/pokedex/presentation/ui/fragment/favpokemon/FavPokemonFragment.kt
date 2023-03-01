package com.kemsky.pokedex.presentation.ui.fragment.favpokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kemsky.pokedex.R
import com.kemsky.pokedex.data.model.PokemonAllModel
import com.kemsky.pokedex.databinding.FragmentFavPokemonBinding
import com.kemsky.pokedex.presentation.ui.fragment.favpokemon.adapter.FavPokemonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavPokemonFragment : Fragment() {

    private val viewModel: FavPokemonViewModel by viewModels()

    private var binding: FragmentFavPokemonBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavPokemonBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureRecyclerView()
    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        binding?.toolbar?.title = getString(R.string.pokedex)
    }

    private fun configureRecyclerView() {
        val pokemonAdapter = FavPokemonAdapter()
        binding?.rvAllPokemon?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.rvAllPokemon?.setHasFixedSize(true)

        binding?.rvAllPokemon?.adapter = pokemonAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getListFav().collect { fav ->
                val listFav = mutableListOf<PokemonAllModel.Result>()
                fav.forEach { model ->
                    PokemonAllModel.Result(model.name, "https://pokeapi.co/api/v2/pokemon/${model.id}/").also {
                        listFav.add(it)
                    }
                }
                pokemonAdapter.submitList(listFav)
            }
        }

        binding?.refresh?.setOnRefreshListener {
            pokemonAdapter.notifyItemChanged(pokemonAdapter.itemCount -1)
            binding?.refresh?.isRefreshing = false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}