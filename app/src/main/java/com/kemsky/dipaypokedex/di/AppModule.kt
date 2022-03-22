package com.kemsky.dipaypokedex.di

import android.content.Context
import com.kemsky.dipaypokedex.ViewModelFactory
import com.kemsky.dipaypokedex.data.remote.ApiService
import com.kemsky.dipaypokedex.data.repository.PokeRepository
import com.kemsky.dipaypokedex.data.repository.PokeRepositoryImpl
import com.kemsky.dipaypokedex.data.room.PokemonDatabase
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
    fun provideViewModelProvider(
        repository: PokeRepository,
        database: PokemonDatabase
    ): ViewModelFactory = ViewModelFactory(
        repository, database
    )


}