package com.anishuu.ui.collection.manga.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_search_fragment,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(SearchMangaQuery(search = "Naruto".toInput())).await()
            } catch (e: ApolloException) {
                Log.d("MangaSearch", "Failure", e)
                null
            }

            val media = response?.data?.page?.media?.filterNotNull()
            Log.i("MangaSearch", media.toString())
            if (media != null && !response.hasErrors()) {
                val adapter = MangaResultsAdapter()
                binding.mangaResults.adapter = adapter
                binding.mangaResults.layoutManager = LinearLayoutManager(requireContext())

                media.let { adapter.submitList(it) }
            }
        }
    }
}