package com.senierr.mortal.ext

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 *
 * @author zhouchunjie
 * @date 2020/12/30 22:19
 */

/**
 * 开启Item拖拽功能
 */
inline fun <reified VH: RecyclerView.ViewHolder> RecyclerView.openItemDrag(
    adapter: RecyclerView.Adapter<VH>, data: MutableList<*>
) {
    val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(adapter, data))
    itemTouchHelper.attachToRecyclerView(this)
}

class ItemTouchCallback<VH: RecyclerView.ViewHolder>(
    val adapter: RecyclerView.Adapter<VH>,
    val data: MutableList<*>
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlags = 0
        if (recyclerView.layoutManager is GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        } else if (recyclerView.layoutManager is LinearLayoutManager) {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 得到当拖拽的viewHolder的Position
        val fromPosition = viewHolder.adapterPosition
        // 拿到当前拖拽到的item的viewHolder
        val toPosition = target.adapterPosition
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 侧滑删除可以使用
    }

    override fun isLongPressDragEnabled(): Boolean = true

    // 原先的Z轴偏移值
    private var rawTranslationZ = 0F

    /**
     * 长按选中Item的时候开始调用
     *
     * @param viewHolder
     * @param actionState
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.itemView?.let {
                it.translationZ = rawTranslationZ + 12F
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 手指松开的时候还原高亮
     *
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.translationZ = rawTranslationZ
        // 完成拖动后刷新适配器，这样拖动后删除就不会错乱
        adapter.notifyDataSetChanged()
    }
}