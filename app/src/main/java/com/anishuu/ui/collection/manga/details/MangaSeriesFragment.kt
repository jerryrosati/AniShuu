/**
 * Defines the fragment responsible for the tab layout that holds a manga's series and collection detail fragments.
 */
package com.anishuu.ui.collection.manga.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.anishuu.R
import com.anishuu.databinding.MangaSeriesFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MangaSeriesFragment : Fragment() {
    private lateinit var binding: MangaSeriesFragmentBinding
    private val fragmentList = mutableListOf<Fragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_series_fragment,
            container,
            false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentList.add(MangaSeriesDetailsFragment())
        fragmentList.add(MangaSeriesCollectionFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.pager.adapter = MangaSeriesPageAdapter(fragmentList, this)

        // Set the tab names.
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = resources.getStringArray(R.array.manga_details_tab_names)[position]
        }.attach()
    }
}