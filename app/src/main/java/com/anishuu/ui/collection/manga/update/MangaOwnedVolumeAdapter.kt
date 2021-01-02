package com.anishuu.ui.collection.manga.update

import com.anishuu.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.anishuu.db.manga.MangaVolume

class MangaOwnedVolumeAdapter : ListAdapter<MangaVolume, MangaOwnedVolumeAdapter.MangaVolumeViewHolder>(
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
        private val volumeView: TextView = itemView.findViewById(R.id.volume_number)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(volume: MangaVolume) {
            volumeView.text = itemView.resources.getString(R.string.volume_number, volume.volumeNum)
            checkbox.isChecked = volume.owned
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                volume.owned = isChecked
            }
        }

        companion object {
            fun create(parent: ViewGroup): MangaVolumeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.manga_volume_adapter_item, parent, false)
                return MangaVolumeViewHolder(view)
            }
        }
    }

    class MangaVolumeComparator : DiffUtil.ItemCallback<MangaVolume>() {
        override fun areItemsTheSame(oldItem: MangaVolume, newItem: MangaVolume): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MangaVolume, newItem: MangaVolume): Boolean {
            return (oldItem.volumeId == newItem.volumeId) && (oldItem.owned == newItem.owned)
        }
    }
}