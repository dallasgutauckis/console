package com.dallasgutauckis.configurator.shared.model

import com.google.gson.annotations.SerializedName

data class RadioGroupNodeData(val name: String,
                              @SerializedName("selected_name") val selectedName: String,
                              val radios: List<RadioData>)