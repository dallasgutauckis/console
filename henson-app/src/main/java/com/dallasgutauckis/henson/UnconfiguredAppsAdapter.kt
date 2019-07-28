package com.dallasgutauckis.henson

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dallasgutauckis.henson.config.model.AvailableApp

class UnconfiguredAppsAdapter(private val list: ArrayList<AvailableApp>,
                              private val eventListener: EventListener)
    : RecyclerView.Adapter<UnconfiguredAppsAdapter.UnconfiguredAppViewHolder>() {

    override fun onBindViewHolder(holder: UnconfiguredAppViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnconfiguredAppViewHolder {
        return UnconfiguredAppViewHolder(UnconfiguredAppItemView(parent.context), eventListener)
    }

    class UnconfiguredAppViewHolder(private val configuredAppItemView: UnconfiguredAppItemView,
                                    private val eventListener: EventListener)
        : RecyclerView.ViewHolder(configuredAppItemView), View.OnClickListener {
        override fun onClick(v: View) {
            val item = v as UnconfiguredAppItemView
            eventListener.onItemClick(item.getApp(), item)
        }

        fun bind(item: AvailableApp) {
            configuredAppItemView.setApp(item)
            configuredAppItemView.setOnClickListener(this)
        }

    }

    interface EventListener {
        fun onItemClick(item: AvailableApp, view: UnconfiguredAppItemView)
    }
}