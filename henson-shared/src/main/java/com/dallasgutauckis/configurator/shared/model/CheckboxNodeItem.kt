package com.dallasgutauckis.configurator.shared.model

import com.google.gson.annotations.SerializedName

data class CheckboxNodeItem(val title: String,
                            val description: String = "",
                            @SerializedName("is_checked") val isChecked: Boolean)