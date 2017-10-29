package com.dallasgutauckis.bloop.bloop

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.dallasgutauckis.bloop.bloop.AvailableAppsAdapter.AvailableAppViewHolder

/**
 * Created by dallas on 2017-10-29.
 */
class AvailableAppsAdapter(val list: ArrayList<AvailableApp>) : RecyclerView.Adapter<AvailableAppViewHolder>() {

    override fun onBindViewHolder(holder: AvailableAppViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableAppViewHolder {
        return AvailableAppViewHolder(AvailableAppItemView(parent.context))
    }

    class AvailableAppViewHolder(private val availableAppItemView: AvailableAppItemView) : ViewHolder(availableAppItemView) {
        fun bind(item: AvailableApp) {
            availableAppItemView.setApp(item)
        }

    }
}