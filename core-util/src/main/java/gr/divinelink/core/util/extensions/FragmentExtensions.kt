package gr.divinelink.core.util.extensions

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.lang.reflect.Field

fun Fragment.addBackPressCallback(
    onBackPressed: OnBackPressedCallback.() -> Unit,
) {
    requireActivity().onBackPressedDispatcher.addBackPressCallback(
        lifecycleOwner = viewLifecycleOwner,
        onBackPressed = onBackPressed,
    )
}

fun FragmentActivity.addBackPressCallback(
    onBackPressed: OnBackPressedCallback.() -> Unit,
) {
    onBackPressedDispatcher.addBackPressCallback(
        lifecycleOwner = { fragmentLifecycleRegistryField.get(this) as Lifecycle },
        onBackPressed = onBackPressed,
    )
}

private val fragmentLifecycleRegistryField: Field by lazy {
    val field = FragmentActivity::class.java.getDeclaredField("mFragmentLifecycleRegistry")
    field.isAccessible = true
    field
}

fun OnBackPressedDispatcher.addBackPressCallback(
    lifecycleOwner: LifecycleOwner,
    onBackPressed: OnBackPressedCallback.() -> Unit,
) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }
    addCallback(lifecycleOwner, callback)
}
