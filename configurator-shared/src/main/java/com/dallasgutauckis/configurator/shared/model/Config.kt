package com.dallasgutauckis.configurator.shared.model

import com.squareup.moshi.*

data class Config(@Json(name = "package_name") val packageName: String,
                  val children: List<Node>) {
    fun toJson(): String {
        return adapter.toJson(this)
    }

    companion object {
        val adapter: JsonAdapter<Config> by lazy {
            val builder = Moshi.Builder()

            builder.add(Node::class.java, object : JsonAdapter<Node>() {
                override fun toJson(writer: JsonWriter, value: Node?) {
                    if (value == null) {
                        writer.nullValue()
                        return
                    }

                    writer.name(Node::type.name).value(value.type.apiName)
                    writer.name(Node::name.name).value(value.name)
                    val jsonAdapter: JsonAdapter<out Any> = Moshi.Builder().build().adapter(value.type.clazz)
                    writer.name(Node::data.name).value(jsonAdapter.toJson())
                }

                override fun fromJson(reader: JsonReader): Node? {
                    var type: String?
                    var dataString: String? = null
                    var name: String? = null
                    var nodeType: NodeType? = null

                    while (reader.hasNext()) {
                        when (reader.nextName()) {
                            Node::type.name -> {
                                type = reader.nextString()
                                nodeType = NodeType.values().filter { it.apiName == type }[0]
                            }
                            Node::name.name -> {
                                name = reader.nextString()
                            }
                            Node::data.name -> {
                                dataString = reader.nextString()
                            }
                        }
                    }

                    checkNotNull(name) {
                        "name must be specified"
                    }

                    checkNotNull(nodeType) {
                        "type must be specified"
                    }

                    checkNotNull(dataString) {
                        "data must be provided"
                    }


                    val dataAdapter = Moshi.Builder().build().adapter(nodeType!!.clazz)

                    return Node(nodeType, name!!, dataAdapter.fromJson(dataString!!))
                }
            })

            builder.add(KotlinJsonAdapterFactory())
            builder.build().adapter(Config::class.java)


        }

        fun fromJson(json: String?): Config? {
            if (json == null) {
                return null
            }

            return adapter.fromJson(json)
        }
    }
}