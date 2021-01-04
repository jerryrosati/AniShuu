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
import timber.log.Timber

/**
 * View Model used to pass Manga details from the [MangaSearchFragment].
 *
 * @property selected The selected [SearchMangaQuery.Medium] object.
 */
class SharedMangaDetailsViewModel : ViewModel() {
    // private val _selected = MutableLiveData<SearchMangaQuery.Medium>()
    val selected = MutableLiveData<SearchMangaQuery.Medium>()
        // get() = _selected

    /**
     * Set the selected Manga series.
     *
     * @param series The selected series.
     */
    fun select(series: SearchMangaQuery.Medium) {
        selected.value = series
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
            Timber.d("Failed to get manga with id $id: $e")
            null
        }

        val mangaResults = response?.data?.page?.media?.filterNotNull()
        selected.value = mangaResults?.first()
    }
}