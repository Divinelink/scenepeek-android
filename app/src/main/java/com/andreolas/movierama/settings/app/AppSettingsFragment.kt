package com.andreolas.movierama.settings.app

import androidx.fragment.app.FragmentTransaction
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.app.appearance.AppearanceSettingsFragment
import com.andreolas.movierama.settings.app.help.HelpSettingsFragment
import com.andreolas.movierama.settings.dsl.DSLConfiguration
import com.andreolas.movierama.settings.dsl.DSLSettingsAdapter
import com.andreolas.movierama.settings.dsl.DSLSettingsFragment
import com.andreolas.movierama.settings.dsl.DSLSettingsIcon
import com.andreolas.movierama.settings.dsl.DSLSettingsText
import com.andreolas.movierama.settings.dsl.configure

class AppSettingsFragment : DSLSettingsFragment(R.string.settings) {

    override fun bindAdapter(adapter: DSLSettingsAdapter) {
        adapter.submitList(getConfiguration().toMappingModelList())
    }

    private fun getConfiguration(): DSLConfiguration {
        return configure {

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__appearance),
                icon = DSLSettingsIcon.from(R.drawable.ic_appearance_24),
                onClick = {
                    updateFragment(AppearanceSettingsFragment())
                }
            )

            dividerPref()

            clickPref(
                title = DSLSettingsText.from(R.string.HelpSettingsFragment__help),
                icon = DSLSettingsIcon.from(R.drawable.ic_help_24),
                onClick = {
                    updateFragment(HelpSettingsFragment())
                }
            )

            dividerPref()
        }
    }

    private fun updateFragment(fragment: DSLSettingsFragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }
}
