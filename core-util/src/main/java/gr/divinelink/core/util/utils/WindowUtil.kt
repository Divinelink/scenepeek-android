package gr.divinelink.core.util.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Window
import androidx.annotation.ColorInt

fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    WindowUtil.setNavigationBarColor(this, this.window, color)
}

object WindowUtil {

    fun setNavigationBarColor(activity: Activity, @ColorInt color: Int) {
        setNavigationBarColor(activity, activity.window, color)
    }

    fun setNavigationBarColor(context: Context, window: Window, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            window.navigationBarColor = ThemeUtil.getThemedColor(context, android.R.attr.navigationBarColor)
        } else {
            window.navigationBarColor = color
        }
    }
}
