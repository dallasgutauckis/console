package com.dallasgutauckis.henson

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dallasgutauckis.henson.config.model.AvailableApp

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
        : RecyclerView.ViewHolder(configuredAppItemView), View.OnClickListener, View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            val item = v as ConfiguredAppItemView
            return eventListener.onItemLongClick(item.getApp(), item)
        }

        override fun onClick(v: View?) {
            val item = v as ConfiguredAppItemView
            eventListener.onItemClick(item.getApp(), item)
        }

        fun bind(item: AvailableApp) {
            configuredAppItemView.setApp(item)
            configuredAppItemView.setOnClickListener(this)
            configuredAppItemView.setOnLongClickListener(this)
        }

    }

    interface EventListener {
        fun onItemClick(item: AvailableApp, view: ConfiguredAppItemView)

        /**
         * @return whether the long click was handled by this callback
         * @see OnLongClickListener#onLongClick
         */
        fun onItemLongClick(item: AvailableApp, view: ConfiguredAppItemView): Boolean
    }
}