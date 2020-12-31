package com.anishuu.ui.collection.manga.search

import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.anishuu.R
import com.anishuu.SearchMangaQuery
import com.anishuu.type.MediaStatus
import com.anishuu.ui.base.BaseSeriesAdapter

/**
 * Adapter for search results when a user searches for a manga on Anilist.
 *
 * @property listener The click listener for the adapter item.
 */
class MangaResultsAdapter(private val listener: (SearchMangaQuery.Medium) -> Unit) :
    BaseSeriesAdapter<SearchMangaQuery.Medium>(MangaResultsComparator()) {

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val result = getItem(position)
        holder.seriesImage.load(result.coverImage?.extraLarge)
        holder.seriesTitle.text = result.title?.romaji
        holder.seriesInfo.text = if (result.status == MediaStatus.FINISHED && result.volumes != null)
            holder.itemView.resources.getString(R.string.manga_status_volumes, result.status.name, result.volumes) else result.status?.name
        holder.itemView.setOnClickListener { listener(result) }
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