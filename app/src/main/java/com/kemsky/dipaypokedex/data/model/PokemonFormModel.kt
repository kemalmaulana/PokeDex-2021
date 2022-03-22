package com.kemsky.dipaypokedex.data.model


import com.google.gson.annotations.SerializedName

data class PokemonFormModel(
    @SerializedName("form_name")
    val formName: String,
    @SerializedName("form_names")
    val formNames: List<Any>,
    @SerializedName("form_order")
    val formOrder: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_battle_only")
    val isBattleOnly: Boolean,
    @SerializedName("is_default")
    val isDefault: Boolean,
    @SerializedName("is_mega")
    val isMega: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("names")
    val names: List<Any>,
    @SerializedName("order")
    val order: Int,
    @SerializedName("pokemon")
    val pokemon: Pokemon,
    @SerializedName("sprites")
    val sprites: Sprites,
    @SerializedName("types")
    val types: List<Type>,
    @SerializedName("version_group")
    val versionGroup: VersionGroup
) {
    data class Pokemon(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )

    data class Sprites(
        @SerializedName("back_default")
        val backDefault: String,
        @SerializedName("back_female")
        val backFemale: Any?,
        @SerializedName("back_shiny")
        val backShiny: String,
        @SerializedName("back_shiny_female")
        val backShinyFemale: Any?,
        @SerializedName("front_default")
        val frontDefault: String,
        @SerializedName("front_female")
        val frontFemale: Any?,
        @SerializedName("front_shiny")
        val frontShiny: String,
        @SerializedName("front_shiny_female")
        val frontShinyFemale: Any?
    )

    data class Type(
        @SerializedName("slot")
        val slot: Int,
        @SerializedName("type")
        val type: Type
    ) {
        data class Type(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }

    data class VersionGroup(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )
}