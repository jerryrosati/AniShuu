package com.anishuu.ui.collection.manga.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anishuu.AnishuuApplication
import com.anishuu.ui.collection.manga.MangaViewModel
import com.anishuu.ui.collection.manga.MangaViewModelFactory
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.anishuu.R
import com.anishuu.databinding.UpdateCollectionFragmentBinding
import com.anishuu.ui.collection.manga.search.SharedMangaDetailsViewModel

class UpdateCollectionFragment : Fragment() {
    private lateinit var binding: UpdateCollectionFragmentBinding
    private lateinit var adapter: MangaOwnedVolumeAdapter
    private val model: SharedMangaDetailsViewModel by activityViewModels()
    private var seriesExistsInDatabase: Boolean = false

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.update_collection_fragment,
            container,
            false)

        adapter = MangaOwnedVolumeAdapter()
        binding.volumeRecyclerview.adapter = adapter
        binding.volumeRecyclerview.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("JERRY", "in onViewCreated")

        // The view model for database operations.
        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        val mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        // The list of volumes for the series.
        var volumeList = mutableListOf<MangaVolume>()

        model.selected.observe(viewLifecycleOwner, Observer {
            if (it.title?.romaji != null) {
                mangaViewModel.doesSeriesExist(it.title.romaji).observe(viewLifecycleOwner, Observer { doesExist ->
                    seriesExistsInDatabase = doesExist
                    Log.i("JERRY", "Series ${it.title.romaji} exists in database: $seriesExistsInDatabase")

                    if (doesExist) {
                        mangaViewModel.getSeries(it.title.romaji).observe(viewLifecycleOwner, Observer { manga ->
                            binding.titleEntry.setText(manga.series.title)
                            binding.numVolumesEntry.setText(if (manga.series.numVolumes > 0) manga.series.numVolumes.toString() else "")
                            binding.authorEntry.setText(manga.series.author)
                            binding.languageEntry.setText(manga.series.language)
                            binding.publisherEntry.setText(manga.series.publisher)
                            binding.notesEntry.setText(manga.series.notes)
                            volumeList.clear()
                            Log.i("JERRY", "Setting volumeList in observe")
                            volumeList = manga.volumes as MutableList<MangaVolume>
                            Log.i("JERRY", "VolumeList = $volumeList")
                            adapter.notifyDataSetChanged()
                            // volumeList.let { volumes -> adapter.submitList(volumes) }
                        })
                    } else {
                        binding.titleEntry.setText(it.title.romaji)
                        binding.numVolumesEntry.setText(it.volumes?.toString())
                    }
                })
            }
        })

        binding.numVolumesEntry.doAfterTextChanged {
            volumeList.clear()

            // Invalidate the previous data in the adapter. SubmitList is hard to use here because we haven't inserted the volume into the DB yet, so
            // we don't have access to an autogenerated volume ID to compare with. This means we're using the volume number to compare, but when we submit
            // a list that has volume numbers that overlap with the current data, that element in the adapter isn't updated and we lose the reference to it,
            // so the owned flag can't be updated.
            adapter.notifyDataSetChanged()

            if (it.toString().isNotEmpty()) {
                Log.i("JERRY", "Setting volumeList in doAfterTextChanged")
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
                val series = MangaSeries(title,
                    numVolumes,
                    language,
                    author,
                    publisher,
                    notes,
                    model.selected.value?.coverImage?.extraLarge ?: "",
                    model.selected.value?.id ?: -1)

                if (seriesExistsInDatabase) {
                    Log.i("UpdateCollectionFragment", "in mangaviewmodel")
                    mangaViewModel.updateSeries(series)
                } else {
                    mangaViewModel.insertSeries(series)
                }

                // Add the volumes to the database.
                for (volume in volumeList) {
                    Log.i("UpdateCollectionFragment", "Volume ${volume.volumeNum} owned = ${volume.owned}")
                    volume.seriesTitle = title
                    if (seriesExistsInDatabase) {
                        Log.i("JERRY", "Updating volume: $volume")
                        mangaViewModel.updateVolume(volume)
                    } else {
                        Log.i("JERRY", "Inserting volume: $volume")
                        mangaViewModel.insertVolume(volume)
                    }
                }

                // Navigate back to the collection screen.
                findNavController().navigate(R.id.collection_dest, null)
            } else {
                Toast.makeText(view.context, getString(R.string.title_required), Toast.LENGTH_SHORT).show()
            }
        }
    }
}