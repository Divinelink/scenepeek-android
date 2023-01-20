package gr.divinelink.core.util.extensions

import android.graphics.drawable.Drawable
import android.view.MenuItem
import gr.divinelink.core.util.extensions.DrawableExtensions.HALF_ALPHA
import gr.divinelink.core.util.extensions.DrawableExtensions.NO_ALPHA

object DrawableExtensions {
    const val HALF_ALPHA = 100
    const val NO_ALPHA = 255
}

fun MenuItem.setDisabled() {
    this.isEnabled = false
    this.icon?.setDisabled()
}

fun MenuItem.setEnabled() {
    this.isEnabled = true
    this.icon?.setEnabled()
}

fun Drawable.setDisabled() {
    this.alpha = HALF_ALPHA
}

fun Drawable.setEnabled() {
    this.alpha = NO_ALPHA
}
