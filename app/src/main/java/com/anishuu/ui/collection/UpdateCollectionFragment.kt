package com.anishuu.ui.collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anishuu.AnishuuApplication
import com.anishuu.MangaViewModel
import com.anishuu.MangaViewModelFactory
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.anishuu.R
import com.anishuu.databinding.UpdateCollectionFragmentBinding

class UpdateCollectionFragment : Fragment() {
    private lateinit var binding: UpdateCollectionFragmentBinding
    private lateinit var adapter: MangaVolumeAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.update_collection_fragment,
            container,
            false)

        adapter = MangaVolumeAdapter()
        binding.volumeRecyclerview.adapter = adapter
        binding.volumeRecyclerview.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        val mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        val volumeList = mutableListOf<MangaVolume>()

        binding.numVolumesEntry.doAfterTextChanged {
            volumeList.clear()
            if (binding.numVolumesEntry.text.toString().isNotEmpty()) {
                for (i in 1..binding.numVolumesEntry.text.toString().toInt()) {
                    Log.i("UpdateCollectionFragment", "Volume = $i")
                    volumeList.add(MangaVolume(i, "", false))
                }

                volumeList.let { adapter.submitList(it) }
                binding.volumeRecyclerview.visibility = View.VISIBLE
            } else {
                binding.volumeRecyclerview.visibility = View.GONE
            }
        }

        // Save the Manga to the database when the save button is pressed.
        binding.buttonSave.setOnClickListener {
            val title = binding.titleEntry.text.toString()

            // Don't let the user proceed without entering a title
            if (title.isNotEmpty()) {
                val numVolumesText = binding.numVolumesEntry.text.toString()
                val numVolumes = if (numVolumesText.isNotEmpty()) numVolumesText.toInt() else 0
                val language = binding.languageEntry.text.toString()
                val author = binding.authorEntry.text.toString()
                val publisher = binding.publisherEntry.text.toString()
                val notes = binding.notesEntry.text.toString()

                // Add the series to the database.
                val series = MangaSeries(title, numVolumes, language, author, publisher, notes)
                mangaViewModel.insertSeries(series)

                // Add the volumes to the database.
                for (volume in volumeList) {
                    Log.i("UpdateCollectionFragment", "Volume ${volume.volumeNum} owned = ${volume.owned}")
                    volume.seriesTitle = title
                    mangaViewModel.insertVolume(volume)
                }

                // Navigate back to the collection screen.
                findNavController().navigate(R.id.collection_dest, null)
            } else {
                Toast.makeText(view.context, getString(R.string.title_required), Toast.LENGTH_SHORT).show()
            }
        }
    }
}