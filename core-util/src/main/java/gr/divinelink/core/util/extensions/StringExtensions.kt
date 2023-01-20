package gr.divinelink.core.util.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned

// Extension method that returns a Spanned text with Html elements.
fun CharSequence.toSpanned(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this.toString())
    }
}
