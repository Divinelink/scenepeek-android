package gr.divinelink.core.util.swipe.utils

import android.util.LayoutDirection
import android.view.View
import androidx.core.text.TextUtilsCompat
import java.util.*

internal fun View.show(isShown: Boolean = false) {
    visibility = if (isShown) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun isLtr(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.LTR
}
