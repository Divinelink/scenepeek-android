package gr.divinelink.core.util.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins

fun View.addSystemWindowInsetToMargin(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
) {
    val (initialLeft, initialTop, initialRight, initialBottom) =
        listOf(marginLeft, marginTop, marginRight, marginBottom)

    val systemBarsInsets = WindowInsetsCompat.Type.systemBars()

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        view.updateLayoutParams {
            (this as? ViewGroup.MarginLayoutParams)?.let {
                updateMargins(
                    left = initialLeft + (if (left) insets.getInsets(systemBarsInsets).left else 0),
                    top = initialTop + (if (top) insets.getInsets(systemBarsInsets).top else 0),
                    right = initialRight + (if (right) insets.getInsets(systemBarsInsets).right else 0),
                    bottom = initialBottom + (if (bottom) insets.getInsets(systemBarsInsets).bottom else 0)
                )
            }
        }

        insets
    }
}
