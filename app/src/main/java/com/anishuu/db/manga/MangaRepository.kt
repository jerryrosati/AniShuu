package com.anishuu.db.manga

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class MangaRepository(private val mangaDao: MangaSeriesDao, private val volumeDao: MangaVolumeDao) {
    // A list of titles stored in alphabetical order.
    val allTitles: Flow<List<Manga>> = mangaDao.getAlphabetizedTitles()

    /**
     * Insert a MangaSeries into the database.
     *
     * @param series The series to insert.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSeries(series: MangaSeries) {
        mangaDao.insert(series)
    }

    /**
     * Get a Manga with the given title from the database.
     *
     * @param title The title of the series to get.
     * @return LiveData containing the Manga.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getSeries(title: String): LiveData<Manga> {
        return mangaDao.getSeries(title)
    }

    /**
     * Insert a MangaVolume into the database.
     *
     * @param volume The volume to insert.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertVolume(volume: MangaVolume) {
        volumeDao.insert(volume)
    }
}