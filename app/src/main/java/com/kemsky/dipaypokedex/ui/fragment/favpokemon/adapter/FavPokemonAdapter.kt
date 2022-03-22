package com.kemsky.dipaypokedex.ui.fragment.favpokemon.adapter

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
import com.kemsky.dipaypokedex.helper.loadImage


class FavPokemonAdapter :
    ListAdapter<PokemonAllModel.Result, FavPokemonAdapter.FavPokemonViewHolder>(
        DiffCallback
    ) {


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
            textTitle.text = item.name

            item.url.substringAfterLast("pokemon/", "").also {
                textNumber.text = String.format("#%s", it)
                avatarPokemon.loadImage(getImageUrl(it))
            }
            layoutTitle.setBackgroundColor(ContextCompat.getColor(this.root.context, R.color.purple_200))
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