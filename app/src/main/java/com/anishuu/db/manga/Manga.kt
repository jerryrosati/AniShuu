package com.anishuu.db.manga

import androidx.room.Embedded
import androidx.room.Relation

data class Manga(
    @Embedded val series: MangaSeries,
    @Relation(
        parentColumn = "title",
        entityColumn = "seriesTitle"
    )
    val volumes: List<MangaVolume>
)