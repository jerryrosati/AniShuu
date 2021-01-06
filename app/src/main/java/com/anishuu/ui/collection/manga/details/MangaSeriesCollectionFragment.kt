package com.anishuu.ui.collection.manga.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.anishuu.AnishuuApplication
import com.anishuu.R
import com.anishuu.databinding.MangaSeriesCollectionFragmentBinding
import com.anishuu.ui.collection.manga.MangaViewModel
import com.anishuu.ui.collection.manga.MangaViewModelFactory
import com.anishuu.ui.collection.manga.SharedMangaDetailsViewModel

class MangaSeriesCollectionFragment : Fragment() {
    private lateinit var binding: MangaSeriesCollectionFragmentBinding
    private lateinit var adapter: MangaViewVolumeAdapter

    private lateinit var mangaViewModel: MangaViewModel
    private val sharedModel: SharedMangaDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_series_collection_fragment,
            container,
            false)

        adapter = MangaViewVolumeAdapter()
        binding.ownedVolumes.adapter = adapter
        binding.ownedVolumes.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the Manga view model.
        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        // Update the displayed manga details.
        sharedModel.selected.observe(viewLifecycleOwner, Observer {
            // If the series is already in the collection, then change the "Add to collection" button to an "Edit Collection" button.
            if (it.title?.romaji != null) {
                mangaViewModel.getSeries(it.title.romaji).observe(viewLifecycleOwner, Observer { manga ->
                    binding.ownedVolumeHeader.visibility = if (manga != null) View.VISIBLE else View.GONE
                    binding.ownedVolumes.visibility = if (manga != null) View.VISIBLE else View.GONE
                    manga?.volumes?.let { volumes -> adapter.submitList(volumes) }
                })
            }
        })
    }

}