package com.anishuu.ui.collection

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.anishuu.*
import com.anishuu.R
import com.anishuu.databinding.CollectionFragmentBinding
import com.anishuu.db.manga.Manga
import com.anishuu.ui.collection.manga.MangaCollectionAdapter
import com.anishuu.ui.collection.manga.MangaViewModel
import com.anishuu.ui.collection.manga.MangaViewModelFactory
import com.anishuu.ui.collection.manga.SharedMangaDetailsViewModel
import timber.log.Timber

class CollectionFragment : Fragment() {
    private lateinit var binding: CollectionFragmentBinding
    private lateinit var adapter: MangaCollectionAdapter
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var mangaToBeDeleted: Manga
    private var isDeleting: Boolean = false

    // Shared Manga Details view model containing data on the selected series.
    private val selectedMangaViewModel: SharedMangaDetailsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.collection_fragment,
            container,
            false)

        // Navigate to the Manga details screen when a title is clicked.
        adapter = MangaCollectionAdapter({ manga ->
            // Turn off the "deleting" mode and invalidate the options menu to hide the deleting button.
            isDeleting = false
            requireActivity().invalidateOptionsMenu()

            selectedMangaViewModel.getMangaById(manga.series.anilistID)
            val action = CollectionFragmentDirections.viewSeries()
            findNavController().navigate(action)
        }, { manga ->
            // Turn on the "deleting" mode and invalidate the options menu to show the deleting button.
            mangaToBeDeleted = manga
            isDeleting = true
            requireActivity().invalidateOptionsMenu()
        })

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(activity, 2)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.collection_fragment_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        Timber.i("In onPrepareOptionsMenu. isDeleting = $isDeleting")
        val item = menu.findItem(R.id.delete_item)
        item.isVisible = isDeleting
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_item -> {
                mangaViewModel.deleteManga(mangaToBeDeleted)
                isDeleting = false
                requireActivity().invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
