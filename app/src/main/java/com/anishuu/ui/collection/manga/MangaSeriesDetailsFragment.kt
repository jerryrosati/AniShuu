package com.anishuu.ui.collection.manga

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
import com.anishuu.databinding.MangaResultDetailsFragmentBinding

class MangaSeriesDetailsFragment : Fragment() {
    private lateinit var binding: MangaResultDetailsFragmentBinding
    private val sharedModel: SharedMangaDetailsViewModel by activityViewModels()
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var adapter: MangaViewVolumeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.manga_result_details_fragment,
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

        binding.collectionButton.setOnClickListener {
            val action = MangaSeriesDetailsFragmentDirections.updateCollection()
            findNavController().navigate(action)
        }

        // Update the displayed manga details.
        sharedModel.selected.observe(viewLifecycleOwner, Observer {
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

            // If the series is already in the collection, then change the "Add to collection" button to an "Edit Collection" button.
            if (it.title?.romaji != null) {
                mangaViewModel.getSeries(it.title.romaji).observe(viewLifecycleOwner, Observer { manga ->
                    binding.collectionButton.text = getString(if (manga != null) R.string.edit_collection else R.string.add_to_collection)
                    binding.ownedVolumeHeader.visibility = if (manga != null) View.VISIBLE else View.GONE
                    binding.ownedVolumes.visibility = if (manga != null) View.VISIBLE else View.GONE
                    manga?.volumes?.let { volumes -> adapter.submitList(volumes) }
                })
            }
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