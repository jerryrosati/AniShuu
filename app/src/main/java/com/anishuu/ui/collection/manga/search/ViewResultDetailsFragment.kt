package com.anishuu.ui.collection.manga.search

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.anishuu.R
import com.anishuu.databinding.MangaResultDetailsFragmentBinding

class ViewResultDetailsFragment : Fragment() {
    private lateinit var binding: MangaResultDetailsFragmentBinding
    private val model: MangaDetailsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_result_details_fragment,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collectionButton.setOnClickListener {
            model.updateEvent(1)
        }

        model.event.observe(viewLifecycleOwner, Observer {
            when(it) {
                1 -> {
                    val action = ViewResultDetailsFragmentDirections.updateCollection()
                    findNavController().navigate(action)
                    model.updateEvent(0)
                }
                else -> { Log.e("ViewResultsDetailsFragment", "Invalid event") }
            }
        })

        // Update the displayed manga details.
        model.selected.observe(viewLifecycleOwner, Observer {
            // Load the images.
            if (it.bannerImage.isNullOrEmpty()) {
                binding.bannerImage.visibility = View.GONE
            } else {
                binding.bannerImage.load(it.bannerImage)
            }
            binding.coverImage.load(it.coverImage?.extraLarge)

            // Update the displayed details.
            binding.mangaTitle.text = it.title?.romaji
            binding.mangaDescription.text = convertHtmlTextToSpanned(it.description)
            binding.status.text = it.status?.name
            binding.startDate.text = getString(R.string.manga_result_details_date, it.startDate?.month, it.startDate?.day, it.startDate?.year)
        })
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