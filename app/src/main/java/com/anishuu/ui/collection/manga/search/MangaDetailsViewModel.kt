package com.anishuu.ui.collection.manga.search

import android.util.Log
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
class MangaDetailsViewModel : ViewModel() {
    val selected = MutableLiveData<SearchMangaQuery.Medium>()

    fun select(series: SearchMangaQuery.Medium) {
        selected.value = series
    }

    fun getMangaById(id: Int) = viewModelScope.launch {
        val response = try {
            apolloClient.query(SearchMangaQuery(id = id.toInput()))
                .await()
        } catch (e: ApolloException) {
            Log.d("MangaDetailsViewModel", "Failed to get Manga with ID $id", e)
            null
        }

        val mangaResults = response?.data?.page?.media?.filterNotNull()
        selected.value = mangaResults?.first()
    }
}