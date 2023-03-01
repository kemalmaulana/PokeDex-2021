package com.kemsky.pokedex.di

import android.content.Context
import com.kemsky.pokedex.data.remote.ApiService
import com.kemsky.pokedex.data.repository.PokeRepository
import com.kemsky.pokedex.data.repository.PokeRepositoryImpl
import com.kemsky.pokedex.data.room.PokemonDatabase
import com.kemsky.pokedex.domain.usecase.all.IPokemonAllRepository
import com.kemsky.pokedex.domain.usecase.all.PokemonAllRepositoryImpl
import com.kemsky.pokedex.domain.usecase.detail.IPokemonDetailRepository
import com.kemsky.pokedex.domain.usecase.detail.PokemonDetailRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(@ApplicationContext context: Context): ApiService = ApiService.invoke(context)

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): PokeRepository = PokeRepositoryImpl(apiService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase =
        PokemonDatabase.getDatabase(context)

    @Provides
    fun provideRepoAllPokemon(repository: PokeRepository): IPokemonAllRepository = PokemonAllRepositoryImpl(repository)

    @Provides
    fun provideRepoDetailPokemon(repository: PokeRepository): IPokemonDetailRepository = PokemonDetailRepositoryImpl(repository)

}