package com.dallasgutauckis.configurator.shared.model

/*

{
package_name: "",
display_type: <(tabs, list)>
children: [{}(TabNode)]

 */
data class RootNode(val type: NodeType,
                    val title: String,
                    val displayType: RootDisplayType,
                    val children: List<RootNode>)



