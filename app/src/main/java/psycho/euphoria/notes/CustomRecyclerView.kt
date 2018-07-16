package psycho.euphoria.notes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet


class CustomRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun canScrollVertically(direction: Int): Boolean {
        if (direction < 1) {
            val original = super.canScrollVertically(direction)
            return !original && getChildAt(0) != null && getChildAt(0).top < 0 || original;
        }
        return super.canScrollVertically(direction)
    }
}

