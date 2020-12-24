package com.anishuu.db.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga_table")
class Manga(@PrimaryKey @ColumnInfo(name = "title") val word: String)