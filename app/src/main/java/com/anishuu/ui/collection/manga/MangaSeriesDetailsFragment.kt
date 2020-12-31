package com.anishuu.ui.collection.manga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.anishuu.AnishuuApplication
import com.anishuu.R
import com.anishuu.databinding.ViewSeriesDetailsFragmentBinding

class MangaSeriesDetailsFragment : Fragment() {
    private lateinit var binding: ViewSeriesDetailsFragmentBinding
    private lateinit var mangaViewModel: MangaViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.view_series_details_fragment,
            container,
            false)

        // Create the Manga view model.
        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs: MangaSeriesDetailsFragmentArgs by navArgs()

        // Display the Manga details.
        mangaViewModel.getSeries(safeArgs.title).observe(viewLifecycleOwner, Observer { manga ->
            val series = manga.series
            binding.numVolumesText.text = getString(R.string.manga_details_num_volumes, series.numVolumes)
            binding.languageText.text = getString(R.string.manga_details_language, series.language)
            binding.authorText.text = getString(R.string.manga_details_author, series.author)
            binding.publisherText.text = getString(R.string.manga_details_publisher, series.publisher)
            binding.notesText.text = getString(R.string.manga_details_notes, series.notes)
            binding.volumesOwnedText.text = getString(R.string.manga_details_volumes_owned,
                manga.volumes.filter { it.owned }.joinToString(", ") { it.volumeNum.toString() })
        })
    }
}