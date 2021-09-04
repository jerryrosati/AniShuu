package com.anishuu.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anishuu.R

/**
 * Base adapter for series items.
 *
 * @param comparator The comparator used to compare items in the list.
 */
abstract class BaseSeriesAdapter<T>(comparator: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseSeriesAdapter.SeriesViewHolder>(comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.manga_series_adapter_item, parent, false)
        return SeriesViewHolder(view)
    }

    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seriesImage: ImageView = itemView.findViewById(R.id.series_image)
        val seriesTitle: TextView = itemView.findViewById(R.id.series_name)
        val seriesInfo: TextView = itemView.findViewById(R.id.series_info)
    }
}