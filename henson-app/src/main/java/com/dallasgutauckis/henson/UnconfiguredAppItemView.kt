package com.dallasgutauckis.henson

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dallasgutauckis.henson.config.model.AvailableApp
import kotterknife.bindView

/**
 * Created by dallas on 2017-10-29.
 */
class UnconfiguredAppItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    val appIcon: ImageView by bindView(R.id.app_icon)
    val appTitle: TextView by bindView(R.id.app_title)

    init {
        View.inflate(getContext(), R.layout.unconfigured_app_item_view, this)
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    private lateinit var appItem: AvailableApp

    fun setApp(value: AvailableApp) {
        appItem = value

        appTitle.text = value.appTitle
        appIcon.setImageDrawable(value.appIcon)
    }

    fun getApp(): AvailableApp {
        return appItem
    }
}