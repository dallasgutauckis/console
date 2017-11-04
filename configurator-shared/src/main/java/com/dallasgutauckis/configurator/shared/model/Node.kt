package com.dallasgutauckis.configurator.shared.model

data class Node(
        val type: NodeType,
        val name: String,
        val data: Any?) {

    companion object {
        fun createTab(name: String, nodes: List<Node>): Node {
            return Node(NodeType.TAB, name, TabNodeData(nodes))
        }

        fun createEditText(hint: String, text: String): EditTextNodeData {
            return EditTextNodeData(hint, text)
        }

        fun createSwitch(name: String, title: String, description: String, enabled: Boolean): SwitchNodeData {
            return SwitchNodeData(name, title, description, enabled)
        }

        fun createGroup(title: String, nodes: List<Node>): GroupNodeData {
            return GroupNodeData(title, nodes)
        }

        fun createCheckbox(name: String, title: String, description: String): CheckboxNodeData {
            return CheckboxNodeData(name, title, description)
        }

        fun createRadioGroup(name: String, selectedName: String, radios: List<RadioData>): RadioGroupNodeData {
            return RadioGroupNodeData(name, selectedName, radios)
        }

        fun createSpinner(name: String, title: String, description: String): SpinnerNodeData {
            return SpinnerNodeData(name, title, description)
        }
    }
}

