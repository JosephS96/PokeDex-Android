package sanchez.jose.pokevision.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class denotes the elements of the main pokemon list
 */

@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val url: String
)