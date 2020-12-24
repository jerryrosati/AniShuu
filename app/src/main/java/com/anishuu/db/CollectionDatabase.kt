package com.anishuu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anishuu.db.manga.Manga
import com.anishuu.db.manga.MangaDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Manga::class), version = 1, exportSchema = false)
public abstract class CollectionDatabase : RoomDatabase() {
    abstract fun mangaDao(): MangaDao

    private class CollectionDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.mangaDao())
                }
            }
        }

        suspend fun populateDatabase(mangaDao: MangaDao) {
            // Delete all content in the database.
            mangaDao.deleteAll()
        }
    }

    companion object {
        // Singleton prevents multiple instances of the database from being open at the same time.
        @Volatile
        private var INSTANCE: CollectionDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CollectionDatabase {
            // If the instance is not null, return it. Otherwise, create the database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CollectionDatabase::class.java,
                    "word_database"
                ).addCallback(
                    CollectionDatabaseCallback(
                        scope
                    )
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}