package com.anishuu.ui.collection.manga.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anishuu.R
import com.anishuu.SearchMangaQuery
import com.anishuu.apolloClient
import com.anishuu.databinding.MangaSearchFragmentBinding
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException

class MangaSearchFragment : Fragment() {
    private lateinit var binding: MangaSearchFragmentBinding
    private lateinit var adapter: MangaResultsAdapter

    // Shared Manga Details view model containing data on the selected series.
    private val model: MangaDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_search_fragment,
            container,
            false)

        // Set click listener for the displayed result items.
        adapter = MangaResultsAdapter() {
            // Update the view model with the manga details.
            model.select(it)

            // Navigate to the manga details screen.
            val action = MangaSearchFragmentDirections.viewResultDetails()
            findNavController().navigate(action)
        }

        binding.mangaResults.adapter = adapter
        binding.mangaResults.layoutManager = GridLayoutManager(activity, 2) // LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Perform an Anilist search when the user presses the search button.
        binding.searchButton.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val response = try {
                    apolloClient.query(SearchMangaQuery(search = binding.searchBox.text.toString().toInput()))
                        .await()
                } catch (e: ApolloException) {
                    Log.d("MangaSearch", "Failure", e)
                    null
                }

                val media = response?.data?.page?.media?.filterNotNull()
                Log.i("MangaSearch", media.toString())

                // Update the RecyclerView data.
                if (media != null && !response.hasErrors()) {
                    media.let { adapter.submitList(it) }
                }
            }
        }
    }
}