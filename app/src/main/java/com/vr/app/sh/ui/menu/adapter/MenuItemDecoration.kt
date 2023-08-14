package com.vr.app.sh.ui.menu.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MenuItemDecoration(val context: Context) : RecyclerView.ItemDecoration()  {

    var leftItem:Int = 4
    var rightItem:Int = 4
    var topItem:Int = 4
    var bottomItem:Int = 4

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        outRect.top = getSize(
            if (itemPosition == 0){ 8 }else{ topItem }
        )
        outRect.bottom = getSize(bottomItem)
        outRect.left = getSize(leftItem)
        outRect.right = getSize(rightItem)
    }

    private fun getSize(i:Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i.toFloat(), context.resources.displayMetrics).toInt()
    }
}