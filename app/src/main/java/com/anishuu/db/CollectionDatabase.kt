package com.anishuu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anishuu.db.manga.MangaSeriesDao
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.anishuu.db.manga.MangaVolumeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [MangaSeries::class, MangaVolume::class], version = 2, exportSchema = false)
abstract class CollectionDatabase : RoomDatabase() {
    abstract fun mangaDao(): MangaSeriesDao
    abstract fun volumeDao(): MangaVolumeDao

    private class CollectionDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.mangaDao(), database.volumeDao())
                }
            }
        }

        suspend fun populateDatabase(mangaDao: MangaSeriesDao, volumeDao: MangaVolumeDao) {
            // Delete all content in the database.
            mangaDao.deleteAll()
            volumeDao.deleteAll()
        }
    }

    companion object {
        // Singleton prevents multiple instances of the database from being open at the same time.
        @Volatile
        private var INSTANCE: CollectionDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CollectionDatabase {
            // If the instance is not null, return it. Otherwise, create the database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, CollectionDatabase::class.java,"word_database")
                    .addCallback(CollectionDatabaseCallback(scope))
                    .fallbackToDestructiveMigration() // TODO 12/28/2020: Add proper migrations.
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}