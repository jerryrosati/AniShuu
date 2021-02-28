/**
 * Defines the adapter for the manga series in the collection.
 */
package com.anishuu.ui.collection.manga

import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.anishuu.R
import com.anishuu.db.manga.Manga
import com.anishuu.ui.base.BaseSeriesAdapter

/**
 * Displays the items in the Manga collection.
 *
 * @property listener The onClickListener that's called when the user clicks on one of the collection items.
 */
class MangaCollectionAdapter(private val listener: (Manga) -> Unit, private val longPressListener: (Manga) -> Unit) :
    BaseSeriesAdapter<Manga>(MangaComparator()) {
    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val current = getItem(position)
        
        if (current.series.imageUrl.isNotEmpty()) {
            holder.seriesImage.load(current.series.imageUrl)
        }
        holder.seriesTitle.text = current.series.title
        holder.seriesInfo.text = holder.itemView.resources.getString(R.string.manga_owned_count, current.volumes.count { it.owned }, current.volumes.size)
        holder.itemView.setOnClickListener { listener(current) }
        holder.itemView.setOnLongClickListener {
            longPressListener(current)
            return@setOnLongClickListener true
        }
    }

    class MangaComparator : DiffUtil.ItemCallback<Manga>() {
        override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
            return oldItem.series.title == newItem.series.title
        }
    }
}