package com.anishuu.db.manga

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class MangaRepository(private val mangaDao: MangaSeriesDao, private val volumeDao: MangaVolumeDao) {
    val allTitles: Flow<List<Manga>> = mangaDao.getAlphabetizedTitles()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSeries(series: MangaSeries) {
        mangaDao.insert(series)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertVolume(volume: MangaVolume) {
        volumeDao.insert(volume)
    }
}