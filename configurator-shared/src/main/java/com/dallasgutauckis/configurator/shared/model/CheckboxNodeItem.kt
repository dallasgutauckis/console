package com.dallasgutauckis.configurator.shared.model

import com.squareup.moshi.Json

data class CheckboxNodeItem(val title: String,
                            val description: String = "",
                            @Json(name = "is_checked") val isChecked: Boolean)