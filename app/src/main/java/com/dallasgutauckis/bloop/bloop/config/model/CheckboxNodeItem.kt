package com.dallasgutauckis.bloop.bloop.config.model

import com.squareup.moshi.Json

data class CheckboxNodeItem(val title: String,
                            val description: String = "",
                            @Json(name = "is_checked") val isChecked: Boolean)