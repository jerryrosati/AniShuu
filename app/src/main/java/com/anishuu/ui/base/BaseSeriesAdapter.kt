package com.anishuu.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anishuu.R

abstract class BaseSeriesAdapter<T>(comparator: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseSeriesAdapter.SeriesViewHolder>(comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.manga_results_adapter_item, parent, false)
        return SeriesViewHolder(view)
    }

    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seriesImage: ImageView
        val seriesTitle: TextView
        val seriesInfo: TextView

        init {
            seriesImage = itemView.findViewById(R.id.series_image)
            seriesTitle = itemView.findViewById(R.id.series_name)
            seriesInfo = itemView.findViewById(R.id.series_info)
        }
    }
}