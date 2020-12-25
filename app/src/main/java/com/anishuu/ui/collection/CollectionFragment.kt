package com.anishuu.ui.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anishuu.*
import com.anishuu.db.CollectionDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rosati.anishuu.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CollectionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MangaAdapter() { manga ->

        }

        val mangaViewModel: MangaViewModel by viewModels {
            MangaViewModelFactory(CollectionDatabase.getDatabase(view.context,
                CoroutineScope(SupervisorJob())))
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Update Manga adapter data when view model is updated.
        mangaViewModel.allTitles.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.submitList(it) }
        })

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null))
    }
}
