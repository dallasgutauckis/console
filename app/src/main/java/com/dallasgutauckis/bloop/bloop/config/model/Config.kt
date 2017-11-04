package com.dallasgutauckis.bloop.bloop.config.model

import com.squareup.moshi.Json

data class Config(@Json(name = "package_name") val packageName: String,
                  val displayType: RootDisplayType,
                  val children: List<Node>)