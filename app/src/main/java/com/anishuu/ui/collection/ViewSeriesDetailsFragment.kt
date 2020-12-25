package com.anishuu.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rosati.anishuu.R

class ViewSeriesDetailsFragment : Fragment() {
    private lateinit var titleTextView: TextView
    private lateinit var numVolumeTextView: TextView
    private lateinit var languageTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var publisherTextView: TextView
    private lateinit var notesTextView: TextView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_series_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numVolumeTextView = view.findViewById(R.id.num_volumes_text)
        languageTextView = view.findViewById(R.id.language_text)
        authorTextView = view.findViewById(R.id.author_text)
        publisherTextView = view.findViewById(R.id.publisher_text)
        notesTextView = view.findViewById(R.id.notes_text)

        val safeArgs: ViewSeriesDetailsFragmentArgs by navArgs()
        numVolumeTextView.text = "Number of volumes: " + safeArgs.numVolumes.toString()
        languageTextView.text = "Language: " + safeArgs.language
        authorTextView.text = "Author: " + safeArgs.author
        publisherTextView.text = "Publisher: " + safeArgs.publisher
        notesTextView.text = "Notes: " + safeArgs.notes
    }
}