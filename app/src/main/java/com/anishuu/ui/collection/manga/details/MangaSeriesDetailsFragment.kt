/**
 * Defines the screen for displaying information about a manga series.
 */
package com.anishuu.ui.collection.manga.details

import android.os.Bundle
import android.text.Html
import android.text.Spanned
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
                    binding.collectionButton.text = getString(if (manga != null) R.string.edit_collection else R.string.add_to_collection)
                })
            }
        }
    }

    /**
     * Converts the HTML text to a Spanned object.
     *
     * @param text The text to convert.
     * @return The HTML text as a Spanned object
     */
    private fun convertHtmlTextToSpanned(text: String?): Spanned {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }
}