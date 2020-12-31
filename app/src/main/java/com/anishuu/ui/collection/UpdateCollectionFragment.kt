package com.anishuu.ui.collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.anishuu.AnishuuApplication
import com.anishuu.MangaViewModel
import com.anishuu.MangaViewModelFactory
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.anishuu.R
import com.anishuu.databinding.UpdateCollectionFragmentBinding
import com.anishuu.ui.collection.manga.search.MangaDetailsViewModel

class UpdateCollectionFragment : Fragment() {
    private lateinit var binding: UpdateCollectionFragmentBinding
    private lateinit var adapter: MangaVolumeAdapter
    private val model: MangaDetailsViewModel by activityViewModels()

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

        model.selected.observe(viewLifecycleOwner, Observer {
            binding.titleEntry.setText(it.title?.romaji)
            binding.numVolumesEntry.setText(it.volumes?.toString())
        })

        binding.numVolumesEntry.doAfterTextChanged {
            volumeList.clear()

            // Invalidate the previous data in the adapter. SubmitList is hard to use here because we haven't inserted the volume into the DB yet, so
            // we don't have access to an autogenerated volume ID to compare with. This means we're using the volume number to compare, but when we submit
            // a list that has volume numbers that overlap with the current data, that element in the adapter isn't updated and we lose the reference to it,
            // so the owned flag can't be updated.
            adapter.notifyDataSetChanged()

            if (it.toString().isNotEmpty()) {
                for (i in 1..binding.numVolumesEntry.text.toString().toInt()) {
                    Log.i("UpdateCollectionFragment", "Volume = $i")
                    volumeList.add(MangaVolume(i, "", false))
                }

                volumeList.let { list -> adapter.submitList(list) }
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
                val series = MangaSeries(title, numVolumes, language, author, publisher, notes, model.selected.value?.coverImage?.extraLarge ?: "")
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