package gr.divinelink.core.util.utils

import android.content.res.Resources
import androidx.annotation.Dimension
import androidx.annotation.Px

enum class DimensionUnit {
    PIXELS {
        @Px
        override fun toPixels(@Px value: Float): Float {
            return value
        }

        @Dimension(unit = Dimension.DP)
        override fun toDp(@Px value: Float): Float {
            return value / Resources.getSystem().displayMetrics.density
        }

        @Dimension(unit = Dimension.SP)
        override fun toSp(@Px value: Float): Float {
            return value / Resources.getSystem().displayMetrics.scaledDensity
        }
    },
    DP {
        @Px
        override fun toPixels(@Dimension(unit = Dimension.DP) value: Float): Float {
            return value * Resources.getSystem().displayMetrics.density
        }

        @Dimension(unit = Dimension.DP)
        override fun toDp(@Dimension(unit = Dimension.DP) value: Float): Float {
            return value
        }

        @Dimension(unit = Dimension.SP)
        override fun toSp(@Dimension(unit = Dimension.DP) value: Float): Float {
            return PIXELS.toSp(toPixels(value))
        }
    },
    SP {
        @Px
        override fun toPixels(@Dimension(unit = Dimension.SP) value: Float): Float {
            return value * Resources.getSystem().displayMetrics.scaledDensity
        }

        @Dimension(unit = Dimension.DP)
        override fun toDp(@Dimension(unit = Dimension.SP) value: Float): Float {
            return PIXELS.toDp(toPixels(value))
        }

        @Dimension(unit = Dimension.SP)
        override fun toSp(@Dimension(unit = Dimension.SP) value: Float): Float {
            return value
        }
    };

    abstract fun toPixels(value: Float): Float
    abstract fun toDp(value: Float): Float
    abstract fun toSp(value: Float): Float
}
