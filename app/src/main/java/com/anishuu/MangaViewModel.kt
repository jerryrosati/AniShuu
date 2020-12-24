package com.anishuu

import androidx.lifecycle.*
import com.anishuu.db.manga.Manga
import com.anishuu.db.manga.MangaRepository
import kotlinx.coroutines.launch

class MangaViewModel(private val repository: MangaRepository) : ViewModel() {
    val allTitles: LiveData<List<Manga>> = repository.allTitles.asLiveData()

    fun insert(manga: Manga) = viewModelScope.launch {
        repository.insert(manga)
    }
}

class MangaViewModelFactory(private val repository: MangaRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MangaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}