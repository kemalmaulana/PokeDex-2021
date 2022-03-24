package com.kemsky.dipaypokedex.ui.fragment.favpokemon.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kemsky.dipaypokedex.R
import com.kemsky.dipaypokedex.constant.AppConstant.getImageUrl
import com.kemsky.dipaypokedex.data.model.PokemonAllModel
import com.kemsky.dipaypokedex.databinding.ItemPokemonBinding
import com.kemsky.dipaypokedex.helper.setImageSrcFromUrlWithLoader
import com.kemsky.dipaypokedex.ui.activity.detailpokemon.DetailActivity
import java.text.DecimalFormat
import java.util.*


class FavPokemonAdapter :
    ListAdapter<PokemonAllModel.Result, FavPokemonAdapter.FavPokemonViewHolder>(
        DiffCallback
    ) {

    private val formatter: DecimalFormat = DecimalFormat("#000")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavPokemonViewHolder {
        return FavPokemonViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavPokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bindPokemon(pokemon)
    }

    inner class FavPokemonViewHolder(private var binding: ItemPokemonBinding) :
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

    companion object DiffCallback : DiffUtil.ItemCallback<PokemonAllModel.Result>() {
        override fun areItemsTheSame(
            oldItem: PokemonAllModel.Result,
            newItem: PokemonAllModel.Result
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: PokemonAllModel.Result,
            newItem: PokemonAllModel.Result
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

}