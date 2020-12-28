package com.anishuu.db.manga

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaSeriesDao {
    @Transaction
    @Query("SELECT * FROM MangaSeries ORDER BY title ASC")
    fun getAlphabetizedTitles(): Flow<List<Manga>>

    @Insert
    suspend fun insert(series: MangaSeries)

    @Update
    suspend fun update(series: MangaSeries)

     @Query("SELECT * from MangaSeries WHERE title = :title")
     fun getSeries(title: String): LiveData<Manga>

    @Query("DELETE FROM MangaSeries")
    suspend fun deleteAll()
}