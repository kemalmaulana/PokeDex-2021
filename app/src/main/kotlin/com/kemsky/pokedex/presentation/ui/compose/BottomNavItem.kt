package com.kemsky.pokedex.presentation.ui.compose

import com.kemsky.pokedex.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object Home : BottomNavItem("Home", R.drawable.ic_home_24, "home")
    object Favorite : BottomNavItem("Favorite", R.drawable.ic_favorite_24, "favorite")
}