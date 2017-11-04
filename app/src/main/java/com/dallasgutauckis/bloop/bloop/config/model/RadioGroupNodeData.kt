package com.dallasgutauckis.bloop.bloop.config.model

import com.squareup.moshi.Json

data class RadioGroupNodeData(val name: String,
                              @Json(name = "selected_name") val selectedName: String,
                              val radios: List<RadioData>)