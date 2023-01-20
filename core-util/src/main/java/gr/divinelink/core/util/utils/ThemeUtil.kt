package gr.divinelink.core.util.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

object ThemeUtil {

    @ColorInt
    fun getThemedColor(context: Context, @AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        return if (theme.resolveAttribute(attr, typedValue, true)) {
            typedValue.data
        } else Color.RED
    }
}
