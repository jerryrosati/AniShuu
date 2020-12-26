package com.anishuu.ui.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anishuu.*
import com.anishuu.db.CollectionDatabase
import com.anishuu.R
import com.anishuu.databinding.CollectionFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CollectionFragment : Fragment() {
    private lateinit var binding: CollectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.collection_fragment,
            container,
            false)

        binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null))

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MangaAdapter() { manga ->
            val series = manga.series
            val action = CollectionFragmentDirections.viewSeries(series.numVolumes,
                series.language,
                series.author,
                series.publisher,
                series.notes)
            findNavController().navigate(action)
        }

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        val mangaViewModel: MangaViewModel by activityViewModels {
            MangaViewModelFactory(CollectionDatabase.getDatabase(
                view.context,
                CoroutineScope(SupervisorJob())))
        }

        // Update Manga adapter data when view model is updated.
        mangaViewModel.allTitles.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.submitList(it) }
        })
    }
}
