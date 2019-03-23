package com.dallasgutauckis.configurator.shared.model

enum class NodeType(val apiName: String, val clazz: Class<out Any>) {
    TAB("tab", TabNodeData::class.java),
    EDITTEXT("edittext", EditTextNodeData::class.java),
    SWITCH("switch", SwitchNodeData::class.java),
    GROUP("group", GroupNodeData::class.java),
    CHECKBOX("checkbox", CheckboxNodeData::class.java),
    RADIO_GROUP("radio_group", RadioGroupNodeData::class.java),
    SPINNER("spinner", SpinnerNodeData::class.java)
}