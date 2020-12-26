package com.anishuu.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anishuu.MangaViewModel
import com.anishuu.MangaViewModelFactory
import com.anishuu.db.CollectionDatabase
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.anishuu.R
import com.anishuu.databinding.UpdateCollectionFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UpdateCollectionFragment : Fragment() {
    private lateinit var binding: UpdateCollectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.update_collection_fragment,
            container,
            false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mangaViewModel: MangaViewModel by viewModels {
            MangaViewModelFactory(CollectionDatabase.getDatabase(view.context,
                CoroutineScope(SupervisorJob())))
        }

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