/**
 * Defines the fragment used to search Anilist for a manga.
 */
package com.anishuu.ui.collection.manga.search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.anishuu.R
import com.anishuu.SearchMangaQuery
import com.anishuu.apolloClient
import com.anishuu.databinding.MangaSearchFragmentBinding
import com.anishuu.ui.collection.manga.SharedMangaDetailsViewModel
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import timber.log.Timber

class MangaSearchFragment : Fragment() {
    private lateinit var binding: MangaSearchFragmentBinding
    private lateinit var adapter: MangaResultsAdapter

    // Results that have been previously displayed.
    private var savedResults = mutableListOf<SearchMangaQuery.Medium>()

    // Shared Manga Details view model containing data on the selected series.
    private val sharedModel: SharedMangaDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_search_fragment,
            container,
            false)

        // Set click listener for the displayed result items.
        adapter = MangaResultsAdapter() {
            // Update the view model with the manga details.
            sharedModel.select(it)

            // Navigate to the manga details screen.
            val action = MangaSearchFragmentDirections.viewResultDetails()
            findNavController().navigate(action)
        }

        binding.mangaResults.adapter = adapter
        binding.mangaResults.layoutManager = GridLayoutManager(activity, 2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display any results that were saved (e.g. in the case of pressing back and entering this fragment).
        if (savedResults.isNotEmpty()) {
            savedResults.let { adapter.submitList(it) }
        }

        // Perform an Anilist search when the user presses the search button or the search button on the keypad
        binding.searchButton.setOnClickListener { searchMangaAndUpdateUI() }
        binding.searchBox.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchMangaAndUpdateUI()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Searches for a manga series on Anilist and update the UI with the results.
     */
    private fun searchMangaAndUpdateUI() {
        // Hide the soft keyboard.
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(SearchMangaQuery(search = binding.searchBox.text.toString().toInput()))
                    .await()
            } catch (e: ApolloException) {
                Timber.d("Failure: $e")
                null
            }

            val mangaResults = response?.data?.page?.media?.filterNotNull()
            Timber.i("Manga Result: ${mangaResults.toString()}")

            // Update the RecyclerView data.
            if (mangaResults != null && !response.hasErrors()) {
                savedResults.addAll(mangaResults)
                mangaResults.let { adapter.submitList(it) }
            }
        }
    }
}