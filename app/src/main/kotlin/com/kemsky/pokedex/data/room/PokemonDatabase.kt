package com.kemsky.pokedex.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kemsky.pokedex.data.room.model.FavPokemonModel

@Database(entities = [FavPokemonModel::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun favPokemonDao(): PokemonDao


    companion object {
        private var INSTANCE: PokemonDatabase? = null
//        private const val DB_NAME = "dipay-fav-pokemon"

        fun getDatabase(context: Context): PokemonDatabase {
            if (INSTANCE == null) {
                synchronized(PokemonDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java).build()
//                            Room.databaseBuilder(context, PokemonDatabase::class.java, DB_NAME)
                                //.allowMainThreadQueries() // Uncomment if you don't want to use RxJava or coroutines just yet (blocks UI thread)
//                                .build()
                    }
                }
            }

            return INSTANCE!!
        }
    }

}