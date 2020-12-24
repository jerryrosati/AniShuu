package com.anishuu.db.manga

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {
    @Query("SELECT * FROM manga_table ORDER BY title ASC")
    fun getAlphabetizedTitles(): Flow<List<Manga>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(manga: Manga)

    @Query("DELETE FROM manga_table")
    suspend fun deleteAll()
}