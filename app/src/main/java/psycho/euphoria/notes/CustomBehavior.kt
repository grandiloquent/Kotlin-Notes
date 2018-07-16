
package psycho.euphoria.notes

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class CustomBehavior : FloatingActionButton.Behavior {
    constructor(context: Context, attributeSet: AttributeSet) : super()

    private val mInterpolator = FastOutSlowInInterpolator()
    private var mIsAnimatingOut = false

    private fun animateOut(button: FloatingActionButton) {
        if (isIceCreamSandwichPlus()) {
            val translationY = (button.height + getMarginBottom(button)).toFloat()
            ViewCompat.animate(button)
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .alpha(0.0f)
                    .translationY(translationY)
                    .setInterpolator(mInterpolator)
                    .setListener(object : ViewPropertyAnimatorListener {
                        override fun onAnimationStart(v: View?) {
                            mIsAnimatingOut = true
                        }

                        override fun onAnimationCancel(v: View?) {
                            mIsAnimatingOut = false
                        }

                        override fun onAnimationEnd(v: View?) {
                            mIsAnimatingOut = false
                            v?.visibility = View.GONE
                        }
                    }).start()
        }

    }

    private fun animateIn(button: FloatingActionButton) {
        if (isIceCreamSandwichPlus()) {
            ViewCompat.animate(button)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1.0f)
                    .translationY(0.0f)
                    .setInterpolator(mInterpolator)
                    .withLayer()
                    .setListener(null)
                    .start()
        }

    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return type == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        type);
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        var canAnimation = true
        if (child is CustomFloatingActionButton) {
            canAnimation = child.canAnimation()
        }
        if (!canAnimation)
            return
        if (dyConsumed > 0 && !this.mIsAnimatingOut && child.visibility === View.VISIBLE) {
            animateOut(child)
        } else if (dyConsumed < 0 && child.visibility !== View.VISIBLE) {
            animateIn(child)
        }
    }

    private fun getMarginBottom(button: FloatingActionButton): Int {
        val layoutParams = button.layoutParams
        return if (layoutParams is ViewGroup.MarginLayoutParams)
            layoutParams.bottomMargin
        else
            0
    }
}
