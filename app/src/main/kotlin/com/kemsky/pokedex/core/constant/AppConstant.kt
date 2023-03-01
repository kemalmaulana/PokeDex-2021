package com.kemsky.pokedex.core.constant

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.kemsky.pokedex.R

object AppConstant {

    const val BASE_URL = "https://pokeapi.co/api/v2/"

    fun getImageUrl(pokeId: String?): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$pokeId.png"


    fun colorByType(context: Context, typeName: String): ColorStateList = when (typeName) {
        "rock" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.rock
                )
            )
        }
        "ghost" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.ghost
                )
            )
        }
        "steel" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.steel
                )
            )
        }
        "water" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.water
                )
            )
        }
        "grass" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.grass
                )
            )
        }
        "physic" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.physic
                )
            )
        }
        "ice" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.ice
                )
            )
        }
        "dark" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.dark
                )
            )
        }
        "fairy" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.fairy
                )
            )
        }
        "normal" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.normal
                )
            )
        }
        "fighting" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.fighting
                )
            )
        }
        "flying" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.flying
                )
            )
        }
        "poison" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.poison
                )
            )
        }
        "ground" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.ground
                )
            )
        }
        "bug" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.bug
                )
            )
        }
        "fire" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.fire
                )
            )
        }
        "electric" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.electric
                )
            )
        }
        "dragon" -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.dragon
                )
            )
        }
        else -> {
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    android.R.color.darker_gray
                )
            )
        }
    }
}