package gr.divinelink.core.util.extensions

import android.graphics.ColorFilter
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback

fun LottieAnimationView.changeLayersColor(
    @ColorRes colorRes: Int,
    keyPaths: KeyPath
) {
    val color = ContextCompat.getColor(context, colorRes)
    val filter = SimpleColorFilter(color)
    val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)
    addValueCallback(keyPaths, LottieProperty.COLOR_FILTER, callback)
}
