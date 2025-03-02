package com.android.eatwell.data.extension.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.eatwell.utilitiesclasses.recyclerView.GridSpacingDecorator

fun RecyclerView.setRecyclerGridLayoutManager(
    numberOfColumn: Int,
    itemSpacing: Int,
    includeEdge: Boolean = true
) {
    val gridLayoutManager = GridLayoutManager(context, numberOfColumn)
    addItemDecoration(GridSpacingDecorator(numberOfColumn, itemSpacing, includeEdge))
    layoutManager = gridLayoutManager
}