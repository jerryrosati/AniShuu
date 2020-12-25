package com.anishuu.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.anishuu.MangaViewModel
import com.anishuu.MangaViewModelFactory
import com.anishuu.db.CollectionDatabase
import com.anishuu.db.manga.MangaSeries
import com.anishuu.db.manga.MangaVolume
import com.rosati.anishuu.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UpdateCollectionFragment : Fragment() {
    private lateinit var titleEditView: EditText
    private lateinit var numVolumeEditView: EditText
    private lateinit var languageEditView: EditText
    private lateinit var authorEditView: EditText
    private lateinit var publisherEditView: EditText
    private lateinit var notesEditView: EditText

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mangaViewModel: MangaViewModel by viewModels {
            MangaViewModelFactory(CollectionDatabase.getDatabase(view.context, CoroutineScope(SupervisorJob())))
        }

        titleEditView = view.findViewById(R.id.title_entry)
        numVolumeEditView = view.findViewById(R.id.num_volumes_entry)
        languageEditView = view.findViewById(R.id.language_entry)
        authorEditView = view.findViewById(R.id.author_entry)
        publisherEditView = view.findViewById(R.id.publisher_entry)
        notesEditView = view.findViewById(R.id.notes_entry)

        val button = view.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val title = titleEditView.text.toString()
            val numVolumes = numVolumeEditView.text.toString().toInt()
            val language = languageEditView.text.toString()
            val author = authorEditView.text.toString()
            val publisher = publisherEditView.text.toString()
            val notes = notesEditView.text.toString()

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