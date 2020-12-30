package com.anishuu.ui.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anishuu.*
import com.anishuu.R
import com.anishuu.databinding.CollectionFragmentBinding

class CollectionFragment : Fragment() {
    private lateinit var binding: CollectionFragmentBinding
    private lateinit var adapter: MangaAdapter
    private lateinit var mangaViewModel: MangaViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.collection_fragment,
            container,
            false)

        // Navigate to the Manga details screen when a title is clicked.
        adapter = MangaAdapter() { manga ->
            val series = manga.series
            val action = CollectionFragmentDirections.viewSeries(series.title)
            findNavController().navigate(action)
        }

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        // Create the Manga view model.
        val application = requireNotNull(this.activity).application
        val mangaViewModelFactory = MangaViewModelFactory(application as AnishuuApplication)
        mangaViewModel = ViewModelProvider(this, mangaViewModelFactory)
            .get(MangaViewModel::class.java)

        // Navigate to the Update Collection screen when the FAB is clicked.
        binding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update Manga adapter data when the view model is updated.
        mangaViewModel.allTitles.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.submitList(it) }
        })
    }
}
