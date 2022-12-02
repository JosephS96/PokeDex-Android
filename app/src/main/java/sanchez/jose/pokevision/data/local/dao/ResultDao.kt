package sanchez.jose.pokevision.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sanchez.jose.pokevision.data.local.entities.ResultEntity

@Dao
interface ResultDao {

    @Query("SELECT * FROM result ORDER BY id ASC")
    fun getPokemonList(): Flow<List<ResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ResultEntity)

    @Insert
    suspend fun insertAllResults(vararg results: ResultEntity)
}