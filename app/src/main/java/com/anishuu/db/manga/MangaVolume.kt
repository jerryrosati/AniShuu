package com.anishuu.db.manga

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MangaVolume(
    val volumeNum: Int = 0,
    var seriesTitle: String = "",
    var owned: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var volumeId: Long = 0L
)