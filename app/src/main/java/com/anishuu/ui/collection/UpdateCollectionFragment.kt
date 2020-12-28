package com.anishuu.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.anishuu.AnishuuApplication
import com.anishuu.MangaViewModel
import com.anishuu.MangaViewModelFactory
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.anishuu.R
import com.anishuu.databinding.UpdateCollectionFragmentBinding

class UpdateCollectionFragment : Fragment() {
    private lateinit var binding: UpdateCollectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.update_collection_fragment,
            container,
            false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        val mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        // Save the Manga to the database when the save button is pressed.
        binding.buttonSave.setOnClickListener {
            val title = binding.titleEntry.text.toString()
            val numVolumes = binding.numVolumesEntry.text.toString().toInt()
            val language = binding.languageEntry.text.toString()
            val author = binding.authorEntry.text.toString()
            val publisher = binding.publisherEntry.text.toString()
            val notes = binding.notesEntry.text.toString()

            // Add the series to the database.
            val series = MangaSeries(title, numVolumes, language, author, publisher, notes)
            mangaViewModel.insertSeries(series)

            // Add numVolumes empty volumes to the database.
            for (i in 0 until numVolumes) {
                mangaViewModel.insertVolume(MangaVolume(i, title, false))
            }

            // Navigate back to the collection screen.
            findNavController().navigate(R.id.collection_dest, null)
        }
    }
}