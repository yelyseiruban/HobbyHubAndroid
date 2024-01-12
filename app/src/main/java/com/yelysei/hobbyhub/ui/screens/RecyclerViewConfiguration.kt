package com.yelysei.hobbyhub.ui.screens

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun recyclerViewConfigureView(configuration: Configuration) {
    //recycler view look configuration
    configuration.recyclerView.layoutManager = configuration.layoutManager
    //recycler view space between items
    configuration.recyclerView.addItemDecoration(
        com.yelysei.hobbyhub.ui.screens.VerticalSpaceItemDecoration(
            configuration.verticalItemSpace
        )
    )
}

data class Configuration(
    val recyclerView: RecyclerView,
    val layoutManager: LinearLayoutManager,
    val verticalItemSpace: Int,
)