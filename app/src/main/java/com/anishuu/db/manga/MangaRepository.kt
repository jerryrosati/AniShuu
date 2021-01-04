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
     * Updates a MangaSeries in the database.
     *
     * @param series The series to update.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateSeries(series: MangaSeries) {
        mangaDao.update(series)
    }

    /**
     * Deletes a [MangaSeries] in the database.
     *
     * @param series The manga series to delete
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteSeries(series: MangaSeries) {
        mangaDao.delete(series)
    }

    /**
     * Get a Manga with the given title from the database.
     *
     * @param title The title of the series to get.
     * @return LiveData containing the Manga.
     */
    @WorkerThread
    fun getSeries(title: String): LiveData<Manga> {
        return mangaDao.getSeries(title)
    }

    /**
     * Checks whether a Manga series with the give title exists in the database.
     *
     * @param title The title to search for.
     * @return True if the title exists, and false otherwise.
     */
    fun doesSeriesExist(title: String): LiveData<Boolean> {
        return mangaDao.doesSeriesExist(title)
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

    /**
     * Update a MangaVolume in the database.
     *
     * @param volume The volume to update.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateVolume(volume: MangaVolume) {
        volumeDao.update(volume)
    }

    /**
     * Get a [MangaVolume] with the given id from the database.
     *
     * @param it The id of the volume to get
     * @return LiveData containing the Volume.
     */
    @WorkerThread
    fun getVolume(id: Long): LiveData<MangaVolume> {
        return volumeDao.getVolume(id)
    }

    /**
     * Deletes a [MangaVolume] in the database.
     *
     * @param volume The volume to delete
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteVolume(volume: MangaVolume) {
        volumeDao.delete(volume)
    }

    /**
     * Deletes all items in the database.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        mangaDao.deleteAll()
        volumeDao.deleteAll()
    }
}