package com.anishuu.ui.collection.manga.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
// import coil.load
import com.anishuu.R
 import com.anishuu.SearchMangaQuery
import com.anishuu.db.manga.Manga

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
        private val author: TextView = itemView.findViewById(R.id.author)

        fun bind(result: SearchMangaQuery.Medium) {
            seriesTitle.text = result.title?.romaji
            Log.i("MangaSearchAdapter", "title = ${result.title?.romaji}")
            author.text = ""
            seriesImage.load(result.coverImage?.extraLarge)
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