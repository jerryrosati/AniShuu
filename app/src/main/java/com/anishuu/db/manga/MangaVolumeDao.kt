package com.anishuu.db.manga

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MangaVolumeDao {
    @Insert
    suspend fun insert(volume: MangaVolume)

    @Update
    suspend fun update(volume: MangaVolume)

    @Delete
    suspend fun delete(volume: MangaVolume)

    @Query("SELECT * FROM MangaVolume ORDER BY volumeNum ASC")
    fun getAllVolumes(): LiveData<List<MangaVolume>>

    @Query("SELECT * from MangaVolume WHERE volumeId = :id")
    fun getVolume(id: Long): LiveData<MangaVolume>

    @Query("DELETE FROM MangaVolume")
    suspend fun deleteAll()
}