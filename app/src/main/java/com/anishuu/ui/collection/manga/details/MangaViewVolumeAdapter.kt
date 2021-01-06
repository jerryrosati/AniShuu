package com.anishuu.ui.collection.manga.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anishuu.R
import com.anishuu.db.manga.MangaVolume

/**
 * Defines the adapter for the owned volume RecyclerView in the MangaSeriesDetailsFragment.
 */
class MangaViewVolumeAdapter : ListAdapter<MangaVolume, MangaViewVolumeAdapter.MangaVolumeViewHolder>(
    MangaVolumeComparator()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaVolumeViewHolder {
        return MangaVolumeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MangaVolumeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MangaVolumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.owned_icon)
        private val volumeNum: TextView = itemView.findViewById(R.id.volume_number)

        fun bind(volume: MangaVolume) {
            volumeNum.text = itemView.resources.getString(R.string.volume_number, volume.volumeNum)
            icon.setImageResource(if (volume.owned) R.drawable.ic_check_mark else R.drawable.ic_x_mark)
        }

        companion object {
            fun create(parent: ViewGroup): MangaVolumeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.manga_owned_volume_adapter_item, parent, false)
                return MangaVolumeViewHolder(view)
            }
        }
    }

    class MangaVolumeComparator : DiffUtil.ItemCallback<MangaVolume>() {
        override fun areItemsTheSame(oldItem: MangaVolume, newItem: MangaVolume): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MangaVolume, newItem: MangaVolume): Boolean {
            return oldItem == newItem
        }
    }
}