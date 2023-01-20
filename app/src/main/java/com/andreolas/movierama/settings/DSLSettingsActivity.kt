package com.andreolas.movierama.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.app.AppSettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.addBackPressCallback

@AndroidEntryPoint
open class DSLSettingsActivity : AppCompatActivity(), DSLSettingsFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dsl_settings_activity)

        if (savedInstanceState == null) {
            val fragment = AppSettingsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commitNow()
        }

        onBackPressedDispatcher.addBackPressCallback(this) { onBackPressed() }
    }

    override fun onNavigateUp(): Boolean {
        return if (!supportFragmentManager.popBackStackImmediate()) {
            onWillFinish()
            finish()
            true
        } else {
            false
        }
    }

    protected open fun onWillFinish() {
        // Intentionally Blank.
    }

    override fun onBackPressed() {
        onNavigateUp()
    }
}
