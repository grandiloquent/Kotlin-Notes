package psycho.euphoria.notes

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

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

class CustomFloatingActionButton : FloatingActionButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    var isForceHide = false
        @SuppressLint("RestrictedApi")
        set(value) {
            field = value
            if (value) visibility = View.GONE
            else visibility = View.VISIBLE
        }

    fun canAnimation() = !isForceHide

}