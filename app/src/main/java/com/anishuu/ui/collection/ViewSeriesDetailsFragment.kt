package com.anishuu.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.anishuu.R
import com.anishuu.databinding.ViewSeriesDetailsFragmentBinding

class ViewSeriesDetailsFragment : Fragment() {
    private lateinit var binding: ViewSeriesDetailsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.view_series_details_fragment,
            container,
            false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs: ViewSeriesDetailsFragmentArgs by navArgs()
        binding.numVolumesText.text = "Number of volumes: " + safeArgs.numVolumes.toString()
        binding.languageText.text = "Language: " + safeArgs.language
        binding.authorText.text = "Author: " + safeArgs.author
        binding.publisherText.text = "Publisher: " + safeArgs.publisher
        binding.notesText.text = "Notes: " + safeArgs.notes
    }
}