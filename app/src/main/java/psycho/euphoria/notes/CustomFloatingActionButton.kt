package psycho.euphoria.notes

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View


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