package com.anishuu.ui.collection.manga.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anishuu.SearchMangaQuery

/**
 * View Model used to pass Manga details from the [MangaSearchFragment].
 *
 * @property selected The selected [SearchMangaQuery.Medium] object.
 */
class MangaDetailsViewModel : ViewModel() {
    val selected = MutableLiveData<SearchMangaQuery.Medium>()

    fun select(series: SearchMangaQuery.Medium) {
        selected.value = series
    }
}