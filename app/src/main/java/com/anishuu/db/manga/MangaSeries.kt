package com.anishuu.db.manga

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MangaSeries")
data class MangaSeries(
    @PrimaryKey val title: String,
    val numVolumes: Int,
    val language: String,
    val author: String,
    val publisher: String,
    val notes: String,
    val imageUrl: String
)