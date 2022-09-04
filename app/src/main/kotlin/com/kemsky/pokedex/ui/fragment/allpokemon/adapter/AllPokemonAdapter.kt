package com.kemsky.pokedex.ui.fragment.allpokemon.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kemsky.pokedex.R
import com.kemsky.pokedex.constant.AppConstant.getImageUrl
import com.kemsky.pokedex.data.model.PokemonAllModel
import com.kemsky.pokedex.databinding.ItemPokemonBinding
import com.kemsky.pokedex.helper.setImageSrcFromUrlWithLoader
import com.kemsky.pokedex.ui.activity.detailpokemon.DetailActivity
import java.text.DecimalFormat
import java.util.*


class AllPokemonAdapter :
    PagingDataAdapter<PokemonAllModel.Result, AllPokemonAdapter.AllPokemonViewHolder>(
        AllPokemonComparator
    ) {

    private val formatter: DecimalFormat = DecimalFormat("#000")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllPokemonViewHolder {
        return AllPokemonViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: AllPokemonViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindPokemon(it) }
    }

    inner class AllPokemonViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPokemon(item: PokemonAllModel.Result) = with(binding) {
            val pokemonName = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val pokemonId = item.url.substringAfterLast("pokemon/", "").replace("/", "")

            textTitle.text = pokemonName
            pokemonId.also {
                textNumber.text = String.format("#%s", formatter.format(it.toInt()))
                avatarPokemon.setImageSrcFromUrlWithLoader(getImageUrl(it), lavLoader)
            }
            layoutTitle.setBackgroundColor(ContextCompat.getColor(this.root.context, R.color.purple_200))
            cardPokemon.setOnClickListener { view ->
                Intent(view.context, DetailActivity::class.java).also {
                    it.putExtra("poke_id", pokemonId.toInt())
                    it.putExtra("poke_name", pokemonName)
                    view.context.startActivity(it)
                }
            }
        }
    }

    object AllPokemonComparator : DiffUtil.ItemCallback<PokemonAllModel.Result>() {
        override fun areItemsTheSame(
            oldItem: PokemonAllModel.Result,
            newItem: PokemonAllModel.Result
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: PokemonAllModel.Result,
            newItem: PokemonAllModel.Result
        ): Boolean {
            return oldItem == newItem
        }
    }
}