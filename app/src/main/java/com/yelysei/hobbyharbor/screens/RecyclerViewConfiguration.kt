package com.yelysei.hobbyharbor.screens

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun recyclerViewConfigureView(configuration: Configuration) {
    //recycler view look configuration
    configuration.recyclerView.layoutManager = configuration.layoutManager
    //recycler view space between items
    configuration.recyclerView.addItemDecoration(
        VerticalSpaceItemDecoration(
            configuration.verticalItemSpace
        )
    )
}

data class Configuration(
    val recyclerView: RecyclerView,
    val layoutManager: LinearLayoutManager,
    val verticalItemSpace: Int,
)