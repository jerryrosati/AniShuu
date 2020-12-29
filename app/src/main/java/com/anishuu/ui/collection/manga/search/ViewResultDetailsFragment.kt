package com.anishuu.ui.collection.manga.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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

        // Update the displayed manga details.
        model.selected.observe(viewLifecycleOwner, Observer {
            binding.coverImage.load(it.coverImage?.extraLarge)
            binding.bannerImage.load(it.bannerImage)
        })
    }
}