package com.anishuu.ui.collection.manga

import androidx.lifecycle.*
import com.anishuu.AnishuuApplication
import com.anishuu.db.manga.Manga
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import kotlinx.coroutines.launch

class MangaViewModel(application: AnishuuApplication) : AndroidViewModel(application) {
    private val repository = application.repository

    // A list of all titles stored in the database.
    val allTitles: LiveData<List<Manga>> = repository.allTitles.asLiveData()

    /**
     * Insert a MangaSeries into the database.
     *
     * @param series The series to insert.
     */
    fun insertSeries(series: MangaSeries) = viewModelScope.launch {
        repository.insertSeries(series)
    }

    /**
     * Get a Manga with the given title from the database.
     *
     * @param title The title of the series to get.
     * @return LiveData containing the Manga.
     */
    fun getSeries(title: String): LiveData<Manga> {
        return repository.getSeries(title)
    }

    /**
     * Insert a MangaVolume into the database.
     *
     * @param volume The volume to insert.
     */
    fun insertVolume(volume: MangaVolume) = viewModelScope.launch {
        repository.insertVolume(volume)
    }
}

class MangaViewModelFactory(private val application: AnishuuApplication) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MangaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}