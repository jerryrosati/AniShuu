package com.anishuu.db.manga

import androidx.lifecycle.LiveData
import androidx.room.*
import timber.log.Timber

@Dao
interface MangaVolumeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(volume: MangaVolume): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(volumeList: List<MangaVolume>): List<Long>

    @Update
    suspend fun update(volume: MangaVolume)

    @Update
    suspend fun update(volumeList: List<MangaVolume>)

    @Delete
    suspend fun delete(volume: MangaVolume)

    @Query("SELECT * FROM MangaVolume ORDER BY volumeNum ASC")
    fun getAllVolumes(): LiveData<List<MangaVolume>>

    @Query("SELECT * from MangaVolume WHERE volumeId = :id")
    fun getVolume(id: Long): LiveData<MangaVolume>

    @Query("DELETE FROM MangaVolume")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertOrUpdate(volume: MangaVolume) {
        val insertResult = insert(volume)
        if (insertResult == -1L) {
            update(volume)
        }
    }

    @Transaction
    suspend fun insertOrUpdate(volumeList: List<MangaVolume>) {
        val updateList = mutableListOf<MangaVolume>()

        // Try to insert the elements in the volume list.
        val insertResultList = insert(volumeList)

        // Add all items that weren't inserted to a list.
        for (i in insertResultList.indices) {
            if (insertResultList[i] == -1L) {
                updateList.add(volumeList[i])
            }
        }

        // Update all of the volumes that weren't inserted.
        if (updateList.isNotEmpty()) {
            Timber.i("Updating list: $updateList")
            update(updateList)
        }
    }
}