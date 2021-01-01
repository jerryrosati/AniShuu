package com.anishuu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // MangaSeries migration.
                database.execSQL("""
                    CREATE TABLE new_MangaSeries (
                            title TEXT PRIMARY KEY NOT NULL,
                            numVolumes INTEGER NOT NULL,
                            language TEXT NOT NULL,
                            author TEXT NOT NULL,
                            publisher TEXT NOT NULL,
                            notes TEXT NOT NULL,
                            imageUrl TEXT NOT NULL DEFAULT '',
                            anilistID INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
                database.execSQL("""
                    INSERT into new_MangaSeries (title, numVolumes, language, author, publisher, notes) 
                    SELECT title, numVolumes, language, author, publisher, notes FROM MangaSeries
                """.trimIndent())
                database.execSQL("DROP TABLE MangaSeries")
                database.execSQL("ALTER TABLE new_MangaSeries RENAME TO MangaSeries")

                // MangaVolume migration.
                database.execSQL("""
                    CREATE TABLE new_MangaVolume (
                            volumeNum INTEGER NOT NULL DEFAULT 0,
                            seriesTitle TEXT NOT NULL DEFAULT '',
                            owned INTEGER NOT NULL DEFAULT 0,
                            volumeId INTEGER PRIMARY KEY NOT NULL DEFAULT 0
                    )
                """.trimIndent())
                database.execSQL("""
                    INSERT into new_MangaVolume (volumeNum, seriesTitle, owned) 
                    SELECT volumeNum, seriesTitle, owned FROM MangaVolume
                """.trimIndent())
                database.execSQL("DROP TABLE MangaVolume")
                database.execSQL("ALTER TABLE new_MangaVolume RENAME TO MangaVolume")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): CollectionDatabase {
            // If the instance is not null, return it. Otherwise, create the database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, CollectionDatabase::class.java,"word_database")
                    .addCallback(CollectionDatabaseCallback(scope))
                    //.addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}