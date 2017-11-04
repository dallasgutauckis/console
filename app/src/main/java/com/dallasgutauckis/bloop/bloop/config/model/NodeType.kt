package com.dallasgutauckis.bloop.bloop.config.model

enum class NodeType(val apiName: String) {
    TAB("tab") {
        fun createData(nodes: List<Node>): TabNodeData {
            return TabNodeData(nodes)
        }
    },
    EDITTEXT("edittext") {
        fun createData(name: String, hint: String, text: String): EditTextNodeData {
            return EditTextNodeData(name, hint, text)
        }
    },
    SWITCH("switch") {
        fun createData(name: String, title: String, description: String, enabled: Boolean): SwitchNodeData {
            return SwitchNodeData(name, title, description, enabled)
        }
    },
    GROUP("group") {
        fun createData(title: String, nodes: List<Node>): GroupNodeData {
            return GroupNodeData(title, nodes)
        }
    },
    CHECKBOX("checkbox") {
        fun createData(name: String, title: String, description: String): CheckboxNodeData {
            return CheckboxNodeData(name, title, description)
        }
    },
    RADIO_GROUP("radio_group") {
        fun createData(name: String, selectedName: String, radios: List<RadioData>): RadioGroupNodeData {
            return RadioGroupNodeData(name, selectedName, radios)
        }
    },
    SPINNER("spinner") {
        fun createData(name: String, title: String, description: String): SpinnerNodeData {
            return SpinnerNodeData(name, title, description)
        }
    };

    open fun create(name: String = "", title: String = "", description: String, data: Any?): Node {
        return Node(this, name, title, description, data)
    }
}