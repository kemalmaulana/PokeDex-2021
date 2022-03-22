package com.kemsky.dipaypokedex.ui.fragment.allpokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kemsky.dipaypokedex.ViewModelFactory
import com.kemsky.dipaypokedex.databinding.FragmentAllPokemonBinding
import com.kemsky.dipaypokedex.ui.fragment.allpokemon.adapter.AllPokemonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllPokemonFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AllPokemonViewModel

    private var binding: FragmentAllPokemonBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllPokemonBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, factory)[AllPokemonViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureRecyclerView()
    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        binding?.toolbar?.title = "All Pokemon"
    }

    private fun configureRecyclerView() {
        val pokemonAdapter = AllPokemonAdapter()
        binding?.rvAllPokemon?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.rvAllPokemon?.setHasFixedSize(true)

        binding?.rvAllPokemon?.adapter = pokemonAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listData.collectLatest { pagedData ->
                pokemonAdapter.submitData(pagedData)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}