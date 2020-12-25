package com.anishuu.db.manga

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MangaVolume(
    @PrimaryKey val volumeNum: Int,
    val seriesTitle: String,
    val owned: Boolean
)