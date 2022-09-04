package com.kemsky.pokedex.data.model


import com.google.gson.annotations.SerializedName

data class PokemonAllModel(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )
}