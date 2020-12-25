package com.anishuu.db.manga

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaSeriesDao {
    @Transaction
    @Query("SELECT * FROM MangaSeries ORDER BY title ASC")
    fun getAlphabetizedTitles(): Flow<List<Manga>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(series: MangaSeries)

    @Query("DELETE FROM MangaSeries")
    suspend fun deleteAll()
}