package com.anishuu.ui.collection.manga

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anishuu.SearchMangaQuery
import com.anishuu.apolloClient
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.launch

/**
 * View Model used to pass Manga details from the [MangaSearchFragment].
 *
 * @property selected The selected [SearchMangaQuery.Medium] object.
 */
class SharedMangaDetailsViewModel : ViewModel() {
    private val _selected = MutableLiveData<SearchMangaQuery.Medium>()
    val selected: LiveData<SearchMangaQuery.Medium>
        get() = _selected

    /**
     * Set the selected Manga series.
     *
     * @param series The selected series.
     */
    fun select(series: SearchMangaQuery.Medium) {
        _selected.value = series
    }

    /**
     * Search Anilist for a manga based on its ID and then set it as the selected item.
     *
     * @param id The Anilist ID of the series.
     */
    fun getMangaById(id: Int) = viewModelScope.launch {
        val response = try {
            apolloClient.query(SearchMangaQuery(id = id.toInput()))
                .await()
        } catch (e: ApolloException) {
            Log.d("MangaDetailsViewModel", "Failed to get Manga with ID $id", e)
            null
        }

        val mangaResults = response?.data?.page?.media?.filterNotNull()
        _selected.value = mangaResults?.first()
    }
}