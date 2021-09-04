/**
 * Defines the screen for displaying information about a manga series.
 */
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
import coil.load
import com.anishuu.util.convertHtmlTextToSpanned
import com.anishuu.AnishuuApplication
import com.anishuu.R
import com.anishuu.databinding.MangaSeriesDetailsFragmentBinding
import com.anishuu.ui.collection.manga.*

class MangaSeriesDetailsFragment : Fragment() {
    private lateinit var binding: MangaSeriesDetailsFragmentBinding
    private val sharedModel: SharedMangaDetailsViewModel by activityViewModels()
    private lateinit var mangaViewModel: MangaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_series_details_fragment,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the Manga view model.
        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        binding.collectionButton.setOnClickListener {
            val action = MangaSeriesFragmentDirections.updateCollection()
            findNavController().navigate(action)
        }

        // Update the displayed manga details.
        val selected = sharedModel.getSelected()
        if (selected != null) {
            // Update the cover image.
            binding.coverImage.load(selected.coverImage?.extraLarge)

            // Update the displayed details.
            binding.mangaTitle.text = selected.title?.romaji
            binding.mangaDescription.text = convertHtmlTextToSpanned(selected.description)
            binding.status.text = selected.status?.name
            binding.startDate.text = getString(R.string.manga_result_details_date, selected.startDate?.month, selected.startDate?.day, selected.startDate?.year)

            // Set the button text to be "Add" or "Edit" depending on whether the series is already stored in the database.
            if (selected.title?.romaji != null) {
                mangaViewModel.getSeries(selected.title.romaji).observe(viewLifecycleOwner, Observer { manga ->
                    val res: Int
                    if (manga != null) {
                        res = R.string.action_button_edit_collection
                    } else {
                        res = R.string.action_button_add_to_collection
                    }

                    binding.collectionButton.setText(getString(res))
                })
            }
        }
    }
}