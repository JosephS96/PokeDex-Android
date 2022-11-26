package sanchez.jose.pokevision.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sanchez.jose.pokevision.data.local.entities.ResultEntity

@Dao
interface ResultDao {

    @Query("SELECT * FROM result ORDER BY id ASC LIMIT :offset")
    fun getPokemonList(limit: Int, offset: Int): Flow<List<ResultEntity>>

    @Insert
    fun insertResult(result: ResultEntity)

    @Insert
    fun insertAllResults(vararg results: ResultEntity)
}