package gr.divinelink.core.util.extensions

import android.content.Intent
import android.os.Build
import android.os.Bundle
import java.io.Serializable

inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
    putExtra(T::class.java.name, victim.ordinal)

inline fun <reified T : Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.java.name, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }

@Suppress("DEPRECATION")
inline fun <reified T : Serializable?> Intent.getSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(key, T::class.java)
    } else {
        this.getSerializableExtra(key) as T
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.getBundleSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}

@Suppress("MagicNumber")
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}
