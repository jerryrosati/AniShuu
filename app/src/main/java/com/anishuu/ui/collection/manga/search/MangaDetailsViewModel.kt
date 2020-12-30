package com.anishuu.ui.collection.manga.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anishuu.SearchMangaQuery

/**
 * View Model used to pass Manga details from the [MangaSearchFragment].
 *
 *
 */
class MangaDetailsViewModel : ViewModel() {
    val event = MutableLiveData<Int>()
    val selected = MutableLiveData<SearchMangaQuery.Medium>()

    fun select(series: SearchMangaQuery.Medium) {
        selected.value = series
    }

    fun updateEvent(newEvent: Int) {
        event.value = newEvent
    }
}