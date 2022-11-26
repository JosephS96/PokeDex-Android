package sanchez.jose.pokevision.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sanchez.jose.pokevision.data.local.entities.PokemonEntity
import sanchez.jose.pokevision.data.remote.responses.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon WHERE id = :id LIMIT 1")
    fun getPokemonById(id: Int): PokemonEntity

    @Insert
    fun insertPokemon(pokemon: Pokemon)

    fun getPokemonList(limit: Int, offset: Int): Flow<PokemonEntity>
}