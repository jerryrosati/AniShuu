/**
 * Defines a View Model for the MangaSearchFragment.
 */
package com.anishuu.ui.collection.manga.search

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

class MangaSearchViewModel : ViewModel() {
    // A list of results from the search.
    private var _mangaResults = MutableLiveData<List<SearchMangaQuery.Medium>>()
    val mangaResults: LiveData<List<SearchMangaQuery.Medium>>
        get() = _mangaResults

    /**
     * Search for a manga title by performing a GraphQL query.
     *
     * @param title The title to search for.
     */
    fun searchManga(title: String) = viewModelScope.launch {
        val response = try {
            apolloClient.query(SearchMangaQuery(search = title.toInput()))
                .await()
        } catch (e: ApolloException) {
            Timber.d("Failure: $e")
            null
        }

        if (response?.hasErrors() == false) {
            _mangaResults.value = response.data?.page?.media?.filterNotNull()
        }
    }
}