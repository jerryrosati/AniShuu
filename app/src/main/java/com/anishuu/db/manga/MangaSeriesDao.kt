package com.anishuu.db.manga

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaSeriesDao {
    @Transaction
    @Query("SELECT * FROM MangaSeries ORDER BY title ASC")
    fun getAlphabetizedTitles(): Single<List<Manga>>

    @Insert
    suspend fun insert(series: MangaSeries)

    @Update
    suspend fun update(series: MangaSeries)

    @Delete
    suspend fun delete(series: MangaSeries)

    @Query("SELECT * from MangaSeries WHERE title = :title")
    fun getSeries(title: String): LiveData<Manga>

    @Query("SELECT EXISTS(SELECT * FROM MangaSeries WHERE title = :title)")
    fun doesSeriesExist(title: String): LiveData<Boolean>

    @Query("DELETE FROM MangaSeries")
    suspend fun deleteAll()
}