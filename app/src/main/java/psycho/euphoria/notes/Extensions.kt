package psycho.euphoria.notes

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar


fun changeTheme(activity: Activity?, theme: Theme) {
    if (activity == null)
        return
    val style: Int
    when (theme) {
        Theme.BROWN -> style = R.style.BrownTheme
        Theme.BLUE -> style = R.style.BlueTheme
        Theme.BLUE_GREY -> style = R.style.BlueGreyTheme
        Theme.YELLOW -> style = R.style.YellowTheme
        Theme.DEEP_PURPLE -> style = R.style.DeepPurpleTheme
        Theme.PINK -> style = R.style.PinkTheme
        Theme.GREEN -> style = R.style.GreenTheme
        else -> style = R.style.RedTheme
    }
    activity.setTheme(style)
}

fun initToolbar(toolbar: Toolbar?, activity: AppCompatActivity?) {
    if (toolbar == null || activity == null)
        return

    toolbar?.apply {
        setBackgroundColor(activity.colorPrimary)
        title = resources.getString(R.string.app_name)
        setTitleTextColor(resources.getColor(R.color.toolbar_title_color))
        collapseActionView()
    }
    activity.supportActionBar?.apply {
        setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        setDisplayHomeAsUpEnabled(true)
    }


}

enum class Theme private constructor(val intValue: Int) {
    default(0x00),
    BROWN(0x01),
    BLUE(0x02),
    BLUE_GREY(0x03),
    YELLOW(0x04),
    DEEP_PURPLE(0x05),
    PINK(0x06),
    GREEN(0x07);


    companion object {

        fun mapValueToTheme(value: Int): Theme {
            for (theme in enumValues<Theme>()) {
                if (value == theme.intValue) {
                    return theme
                }
            }
            // If run here, return default
            return default
        }
    }
}