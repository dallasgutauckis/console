package com.dallasgutauckis.configurator.shared.model

import com.google.gson.annotations.SerializedName

data class Config(
        @SerializedName("package_name") val packageName: String,
        val children: List<TabNode>
)