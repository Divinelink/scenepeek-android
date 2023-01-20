package gr.divinelink.core.util.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import gr.divinelink.core.util.utils.DimensionUnit
import kotlin.math.min

class SlideUpWithSnackbarBehavior(context: Context, attributeSet: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attributeSet) {
    @Px
    private val padTopOfSnackbar: Float = DimensionUnit.DP.toPixels(PAD_TOP_OF_SNACKBAR_DP)
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val translationY = min(0f, dependency.translationY - (dependency.height + padTopOfSnackbar))
        child.animate().translationY(translationY).duration = SLIDE_DURATION
        return true
    }

    override fun onDependentViewRemoved(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ) {
        child.animate().translationY(0f).duration = SLIDE_DURATION
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is SnackbarLayout
    }

    companion object {
        @Dimension(unit = Dimension.DP)
        private val PAD_TOP_OF_SNACKBAR_DP = 8f
        private const val SLIDE_DURATION = 200L
    }
}
