package com.kemsky.dipaypokedex.data.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.kemsky.dipaypokedex.constant.AppConstant.BASE_URL
import com.kemsky.dipaypokedex.data.model.PokemonAllModel
import com.kemsky.dipaypokedex.data.model.PokemonDetailModel
import com.kemsky.dipaypokedex.data.model.PokemonFormModel
import com.kemsky.dipaypokedex.data.model.PokemonSpeciesModel
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("/api/v2//pokemon")
    suspend fun getAllPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonAllModel>

    @GET("/api/v2//pokemon/{pokemonId}")
    suspend fun getDetailPokemon(
        @Path("pokemonId") pokemonId: Int
    ): Response<PokemonDetailModel>

    @GET("/api/v2/pokemon-form/{pokemonId}")
    suspend fun getFormPokemon(
        @Path("pokemonId") pokemonId: Int
    ): Response<PokemonFormModel>

    @GET("/api/v2/pokemon-species/{pokemonId}")
    suspend fun getSpeciesPokemon(
        @Path("pokemonId") pokemonId: Int
    ): Response<PokemonSpeciesModel>


    companion object {
        private const val TIME_OUT = 60_000
        operator fun invoke(context: Context): ApiService {
            val gson = GsonBuilder().create()
            val client = OkHttpClient.Builder()
                .addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .collector(ChuckerCollector(context))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(ApiService::class.java)
        }
    }
}