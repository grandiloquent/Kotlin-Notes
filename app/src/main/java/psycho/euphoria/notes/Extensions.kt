package psycho.euphoria.notes

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.widget.ImageView

fun ImageView.setVectorCompat(@DrawableRes drawable: Int, tint: Int? = null) {
    val vector = VectorDrawableCompat.create(resources, drawable, context.theme)
    if (tint != null) {
        vector?.mutate()
        vector?.setTint(tint)
    }
}
fun Context.getResourceColor(@StringRes resource: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(resource))
    val attrValue = typedArray.getColor(0, 0)
    typedArray.recycle()
    return attrValue
}