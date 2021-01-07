package com.senierr.base.support.ui.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 网格模式分割
 *
 * @author zhouchunjie
 * @date 2018/5/6
 */
class GridItemDecoration(
    private val dividerSize: Int,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            if (includeEdge) {
                outRect.left = dividerSize - column * dividerSize / spanCount
                outRect.right = (column + 1) * dividerSize / spanCount

                if (position < spanCount) {
                    outRect.top = dividerSize
                }
                outRect.bottom = dividerSize
            } else {
                outRect.left = column * dividerSize / spanCount
                outRect.right = dividerSize - (column + 1) * dividerSize / spanCount
                if (position >= spanCount) {
                    outRect.top = dividerSize
                }
            }
        }
    }
}