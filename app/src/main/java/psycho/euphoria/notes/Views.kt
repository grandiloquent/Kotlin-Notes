package psycho.euphoria.notes


import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.Context
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.common_view_empty.view.*

class EmptyView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        RelativeLayout(context, attrs) {

    init {
        inflate(context, R.layout.common_view_empty, this)
    }

    /**
     * Hide the information view
     */
    fun hide() {
        this.visibility = View.GONE
    }

    /**
     * Show the information view
     * @param drawable icon of information view
     * @param textResource text of information view
     */
    fun show(drawable: Int, textResource: Int) {
        image_view.setVectorCompat(drawable, context.getResourceColor(android.R.attr.textColorHint))
        text_label.text = context.getString(textResource)
        this.visibility = View.VISIBLE
    }
}

class ElevationAppBarLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppBarLayout(context, attrs) {
    private var origStateAnimator: StateListAnimator? = null

    init {
        if (Build.VERSION.SDK_INT >= 21) {
            origStateAnimator = stateListAnimator
        }
    }

    fun enableElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            stateListAnimator = origStateAnimator
        }
    }

    fun disableElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            stateListAnimator = StateListAnimator().apply {
                val objAnimator = ObjectAnimator.ofFloat(this, "elevation", 0f)

                addState(intArrayOf(android.R.attr.enabled, R.attr.state_collapsible, -R.attr.state_collapsed),
                        objAnimator)

                addState(intArrayOf(android.R.attr.enabled), objAnimator)

                addState(IntArray(0), objAnimator)
            }
        }
    }
}