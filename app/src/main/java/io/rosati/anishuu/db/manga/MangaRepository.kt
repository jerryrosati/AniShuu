package io.rosati.anishuu.db.manga

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class MangaRepository(private val mangaDao: MangaDao) {
    val allTitles: Flow<List<Manga>> = mangaDao.getAlphabetizedTitles()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(manga: Manga) {
        mangaDao.insert(manga)
    }
}