package sanchez.jose.pokevision.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sanchez.jose.pokevision.data.local.dao.PokemonDao
import sanchez.jose.pokevision.data.local.dao.ResultDao
import sanchez.jose.pokevision.data.local.entities.PokemonEntity
import sanchez.jose.pokevision.data.local.entities.ResultEntity

@Database(entities =
    [
        PokemonEntity::class,
        ResultEntity::class
    ]
    , version = 1)
abstract class PokeVisionDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile private var instance: PokeVisionDatabase? = null

        fun getInstance(context: Context): PokeVisionDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PokeVisionDatabase {
            return Room
                .databaseBuilder(context, PokeVisionDatabase::class.java, "PokeVisionDatabase")
                .build()
        }
    }
}