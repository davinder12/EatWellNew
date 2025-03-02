package com.android.eatwell.utilitiesclasses.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingDecorator(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing /
                    spanCount // ITEM_SPACING - column * ((1f / spanCount) * ITEM_SPACING)
            outRect.right = (column + 1) * spacing /
                    spanCount // (column + 1) * ((1f / spanCount) * ITEM_SPACING)

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left =
                column * spacing / spanCount // column * ((1f / spanCount) * ITEM_SPACING)
            outRect.right = spacing - (column + 1) * spacing /
                    spanCount // ITEM_SPACING - (column + 1) * ((1f /    spanCount) * ITEM_SPACING)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}