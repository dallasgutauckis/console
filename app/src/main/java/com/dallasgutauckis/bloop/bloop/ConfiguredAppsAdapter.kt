package com.dallasgutauckis.bloop.bloop

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup

class ConfiguredAppsAdapter(private val list: ArrayList<AvailableApp>,
                           private val eventListener: EventListener)
    : RecyclerView.Adapter<ConfiguredAppsAdapter.AvailableAppViewHolder>() {

    override fun onBindViewHolder(holder: AvailableAppViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableAppViewHolder {
        return AvailableAppViewHolder(ConfiguredAppItemView(parent.context), eventListener)
    }

    class AvailableAppViewHolder(private val configuredAppItemView: ConfiguredAppItemView,
                                 private val eventListener: EventListener)
        : ViewHolder(configuredAppItemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val item = v as ConfiguredAppItemView
            eventListener.onItemClick(item.getApp(), item)
        }

        fun bind(item: AvailableApp) {
            configuredAppItemView.setApp(item)
            configuredAppItemView.setOnClickListener(this)
        }

    }

    interface EventListener {
        fun onItemClick(item: AvailableApp, view: ConfiguredAppItemView)
    }
}