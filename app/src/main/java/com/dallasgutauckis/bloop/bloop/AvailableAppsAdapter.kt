package com.dallasgutauckis.bloop.bloop

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.dallasgutauckis.bloop.bloop.AvailableAppsAdapter.AvailableAppViewHolder

/**
 * Created by dallas on 2017-10-29.
 */
class AvailableAppsAdapter(private val list: ArrayList<AvailableApp>,
                           private val eventListener: EventListener)
    : RecyclerView.Adapter<AvailableAppViewHolder>() {

    override fun onBindViewHolder(holder: AvailableAppViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableAppViewHolder {
        return AvailableAppViewHolder(AvailableAppItemView(parent.context), eventListener)
    }

    class AvailableAppViewHolder(private val availableAppItemView: AvailableAppItemView,
                                 private val eventListener: EventListener)
        : ViewHolder(availableAppItemView), View.OnClickListener {
        override fun onClick(v: View?) {
            val item = v as AvailableAppItemView
            eventListener.onCardClick(item.getApp(), item)
        }

        fun bind(item: AvailableApp) {
            availableAppItemView.setApp(item)
            availableAppItemView.setOnClickListener(this)
        }

    }

    interface EventListener {
        fun onCardClick(item: AvailableApp, view: AvailableAppItemView)
    }
}