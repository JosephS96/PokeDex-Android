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
    suspend fun getPokemonById(id: Int): PokemonEntity

    @Insert
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon LIMIT :limit")
    fun getPokemonList(limit: Int): Flow<PokemonEntity>
}