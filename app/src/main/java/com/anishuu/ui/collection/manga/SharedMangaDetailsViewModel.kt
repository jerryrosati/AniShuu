package com.anishuu.ui.collection.manga

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anishuu.SearchMangaQuery
import com.anishuu.apolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx3.rxQuery
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * View Model used to pass Manga details from the [MangaSearchFragment].
 *
 * @property selected The selected [SearchMangaQuery.Medium] object.
 */
class SharedMangaDetailsViewModel : ViewModel() {
    private lateinit var selected: SearchMangaQuery.Medium

    /**
     * Set the selected Manga series.
     *
     * @param series The selected series.
     */
    fun select(series: SearchMangaQuery.Medium) {
        selected = series
    }

    fun getSelected(): SearchMangaQuery.Medium {
        return selected
    }

    /**
     * Search Anilist for a manga based on its ID and then set it as the selected item.
     *
     * @param id The Anilist ID of the series.
     */
    fun getMangaById(id: Int): Observable<Response<SearchMangaQuery.Data>> {
        val query = SearchMangaQuery(id = id.toInput())
        return apolloClient.rxQuery(query)
    }
}