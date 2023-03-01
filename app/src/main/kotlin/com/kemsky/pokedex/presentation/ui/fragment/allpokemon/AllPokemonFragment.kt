package com.kemsky.pokedex.presentation.ui.fragment.allpokemon

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import com.kemsky.pokedex.R
import com.kemsky.pokedex.databinding.FragmentAllPokemonBinding
import com.kemsky.pokedex.presentation.ui.fragment.allpokemon.adapter.AllPokemonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AllPokemonFragment : Fragment(), MenuProvider {

    private val viewModel: AllPokemonViewModel by viewModels()

    private var binding: FragmentAllPokemonBinding? = null

    private val pokemonAdapter = AllPokemonAdapter()
    private lateinit var searchView: SearchView
    private lateinit var queryTextListener: SearchView.OnQueryTextListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllPokemonBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true)
        configureToolbar()
        configureRecyclerView()
    }

    private fun configureToolbar() {
        binding?.toolbar?.title = getString(R.string.pokedex)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
    }

    private fun configureRecyclerView() {
        pokemonAdapter.refresh()
        binding?.rvAllPokemon?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.rvAllPokemon?.setHasFixedSize(true)

        binding?.rvAllPokemon?.adapter = pokemonAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.listData.collectLatest { pagedData ->
                pokemonAdapter.submitData(pagedData)
            }
        }

        binding?.refresh?.setOnRefreshListener {
            pokemonAdapter.refresh()
            binding?.refresh?.isRefreshing = false
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        return super.onOptionsItemSelected(item)
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.searchable_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchView.isIconified = true
            searchView.setIconifiedByDefault(true)
        }

        if (::searchView.isInitialized) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    // OnChange
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    // OnSubmit
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.listData.collectLatest { pagedData ->
                            val paged = when {
                                !query.isNullOrBlank() -> {
                                    pagedData.filter { result ->
                                        result.name.lowercase().startsWith(query.toString())
                                    }
                                }
                                else -> pagedData
                            }
                            Timber.e("Query: $query")
                            pokemonAdapter.submitData(paged)
                            pokemonAdapter.notifyItemChanged(0)
                        }
                    }
                    return true
                }
            }
            searchView.setOnQueryTextListener(queryTextListener)

            searchView.setOnCloseListener {
                Timber.e("Closed")
                configureRecyclerView()
                return@setOnCloseListener false
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_search ->
                return false
            else -> {}
        }
        searchView.setOnQueryTextListener(queryTextListener)
        return true
    }
}