package com.anishuu.ui.collection.manga.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.anishuu.R
 import com.anishuu.SearchMangaQuery

/**
 * Adapter for search results when a user searches for a manga on Anilist.
 *
 * @property listener The click listener for the adapter item.
 */
class MangaResultsAdapter(private val listener: (SearchMangaQuery.Medium) -> Unit) :
    ListAdapter<SearchMangaQuery.Medium, MangaResultsAdapter.MangaResultsViewHolder>(MangaResultsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaResultsViewHolder {
        return MangaResultsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MangaResultsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener { listener(current) }
    }

    class MangaResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val seriesImage: ImageView = itemView.findViewById(R.id.series_image)
        private val seriesTitle: TextView = itemView.findViewById(R.id.series_name)
        private val seriesInfo: TextView = itemView.findViewById(R.id.series_info)

        fun bind(result: SearchMangaQuery.Medium) {
            seriesImage.load(result.coverImage?.extraLarge)
            seriesTitle.text = result.title?.romaji
            seriesInfo.text = result.status?.name
        }

        companion object {
            fun create(parent: ViewGroup): MangaResultsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.manga_results_adapter_item, parent, false)
                return MangaResultsViewHolder(view)
            }
        }
    }

    class MangaResultsComparator : DiffUtil.ItemCallback<SearchMangaQuery.Medium>() {
        override fun areItemsTheSame(oldItem: SearchMangaQuery.Medium, newItem: SearchMangaQuery.Medium): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SearchMangaQuery.Medium, newItem: SearchMangaQuery.Medium): Boolean {
            return oldItem == newItem
        }
    }
}