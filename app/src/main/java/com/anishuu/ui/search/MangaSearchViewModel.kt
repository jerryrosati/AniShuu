/**
 * Defines a View Model for the MangaSearchFragment.
 */
package com.anishuu.ui.search

import androidx.lifecycle.ViewModel
import com.anishuu.SearchMangaQuery
import com.anishuu.apolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.rx3.rxQuery
import io.reactivex.rxjava3.core.Observable

class MangaSearchViewModel : ViewModel() {
    /**
     * Search for a manga title by performing a GraphQL query.
     *
     * @param title The title to search for.
     */
    fun searchManga(title: String): Observable<Response<SearchMangaQuery.Data>> {
        val query = SearchMangaQuery(search = title.toInput())
        return apolloClient.rxQuery(query)
    }
}