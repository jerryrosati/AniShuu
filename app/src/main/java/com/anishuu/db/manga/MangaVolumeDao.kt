package com.anishuu.db.manga

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MangaVolumeDao {
    @Insert
    suspend fun insert(volume: MangaVolume)

    @Update
    suspend fun update(volume: MangaVolume)

    @Query("SELECT * FROM MangaVolume ORDER BY volumeNum ASC")
    fun getAllVolumes(): LiveData<List<MangaVolume>>

    @Query("DELETE FROM MangaSeries")
    suspend fun deleteAll()
}