package com.anishuu.db.manga

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaVolumeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(volume: MangaVolume)

    @Query("DELETE FROM MangaSeries")
    suspend fun deleteAll()
}