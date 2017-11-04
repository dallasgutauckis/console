package com.dallasgutauckis.configurator.shared.model

enum class NodeType(val apiName: String) {
    TAB("tab"),
    EDITTEXT("edittext"),
    SWITCH("switch"),
    GROUP("group"),
    CHECKBOX("checkbox"),
    RADIO_GROUP("radio_group"),
    SPINNER("spinner")

    open fun create(name: String = "", title: String = "", description: String, data: Any?): Node {
        return Node(this, name, title, description, data)
    }
}