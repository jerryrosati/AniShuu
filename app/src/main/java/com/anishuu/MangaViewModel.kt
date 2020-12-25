package com.anishuu

import androidx.lifecycle.*
import com.anishuu.db.CollectionDatabase
import com.anishuu.db.manga.Manga
import com.anishuu.db.manga.MangaRepository
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import kotlinx.coroutines.launch

class MangaViewModel(private val database: CollectionDatabase) : ViewModel() {
    private val repository = MangaRepository(database.mangaDao(), database.volumeDao())
    val allTitles: LiveData<List<Manga>> = repository.allTitles.asLiveData()

    fun insertSeries(series: MangaSeries) = viewModelScope.launch {
        repository.insertSeries(series)
    }

    fun insertVolume(volume: MangaVolume) = viewModelScope.launch {
        repository.insertVolume(volume)
    }
}

class MangaViewModelFactory(private val database: CollectionDatabase) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MangaViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}