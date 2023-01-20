@file:Suppress("MagicNumber")
package gr.divinelink.core.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.widget.TextView

fun View?.fadeOut() {
    this?.animate()
        ?.alpha(0f)
        ?.setDuration(500L)
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@fadeOut.alpha = 1f
                this@fadeOut.visibility = View.GONE
            }
        })
}

fun View?.fadeIn() {
    this?.alpha = 0f
    this?.animate()
        ?.alpha(1f)
        ?.setDuration(500L)
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@fadeIn.alpha = 1f
                this@fadeIn.visibility = View.VISIBLE
            }
        })
}

fun View?.updatePaddingAnimator(
    start: Int? = null,
    end: Int? = null,
    top: Int? = null,
    bottom: Int? = null
) {
    if (this == null) return
    start?.let { ValueAnimator.ofInt(this.paddingStart, it) }
        ?.also { it.addUpdateListener { valueAnimator -> this.padding(left = valueAnimator.animatedValue as Int) } }
        ?.also { it.duration = 200 }
        ?.run { start() }

    end?.let { ValueAnimator.ofInt(this.paddingEnd, it) }
        ?.also { it.addUpdateListener { valueAnimator -> this.padding(right = valueAnimator.animatedValue as Int) } }
        ?.also { it.duration = 200 }
        ?.run { start() }

    top?.let { ValueAnimator.ofInt(this.paddingStart, it) }
        ?.also { it.addUpdateListener { valueAnimator -> this.padding(top = valueAnimator.animatedValue as Int) } }
        ?.also { it.duration = 200 }
        ?.run { start() }

    bottom?.let { ValueAnimator.ofInt(this.paddingBottom, it) }
        ?.also { it.addUpdateListener { valueAnimator -> this.padding(bottom = valueAnimator.animatedValue as Int) } }
        ?.also { it.duration = 200 }
        ?.run { start() }
}

var View.visible: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

infix fun TextView?.updateTextWithFade(text: CharSequence) {
    this?.animate()
        ?.alpha(0f)
        ?.setDuration(500L)
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@updateTextWithFade.text = text
                this@updateTextWithFade.animate()
                    ?.alpha(1f)?.duration = 500L
            }
        })
}

fun View.padding(left: Int = paddingLeft, top: Int = paddingTop, right: Int = paddingRight, bottom: Int = paddingBottom) {
    setPadding(left, top, right, bottom)
}
