package com.dallasgutauckis.configurator.shared.model

data class Node(
        val type: NodeType,
        val name: String,
        val data: Any?) {

    companion object {
        fun createTab(name: String, title: String, items: List<Node>): Node {
            return Node(NodeType.TAB, name, TabNodeData(title, items))
        }

        fun createEditText(name: String, hint: String, text: String): Node {
            return Node(NodeType.EDITTEXT, name, EditTextNodeData(hint, text))
        }

        fun createSwitch(name: String, title: String, description: String?, enabled: Boolean): Node {
            return Node(NodeType.SWITCH, name, SwitchNodeData(name, title, description, enabled))
        }

        fun createGroup(name: String, title: String, nodes: List<Node>): Node {
            return Node(NodeType.GROUP, name, GroupNodeData(title, nodes))
        }

        fun createCheckbox(name: String, title: String, description: String?): Node{
            return Node(NodeType.CHECKBOX, name, CheckboxNodeData(name, title, description))
        }

        fun createRadioGroup(name: String, selectedName: String, radios: List<RadioData>): Node {
            return Node(NodeType.RADIO_GROUP, name, RadioGroupNodeData(name, selectedName, radios))
        }

        fun createSpinner(name: String, title: String, description: String?): Node {
            return Node(NodeType.SPINNER, name, SpinnerNodeData(name, title, description))
        }
    }
}

