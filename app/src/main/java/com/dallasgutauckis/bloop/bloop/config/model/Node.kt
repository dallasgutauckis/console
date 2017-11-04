package com.dallasgutauckis.bloop.bloop.config.model

data class Node(
        val type: NodeType,
        val name: String,
        val title: String,
        val description: String = "",
        val data: Any?
)