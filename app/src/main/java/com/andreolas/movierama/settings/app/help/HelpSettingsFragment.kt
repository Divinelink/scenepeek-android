package com.andreolas.movierama.settings.app.help

import com.andreolas.movierama.BuildConfig
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.dsl.DSLConfiguration
import com.andreolas.movierama.settings.dsl.DSLSettingsAdapter
import com.andreolas.movierama.settings.dsl.DSLSettingsFragment
import com.andreolas.movierama.settings.dsl.DSLSettingsText
import com.andreolas.movierama.settings.dsl.configure
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpSettingsFragment : DSLSettingsFragment(R.string.HelpSettingsFragment__help) {
    override fun bindAdapter(adapter: DSLSettingsAdapter) {
        adapter.submitList(getConfiguration().toMappingModelList())
    }

    private fun getConfiguration(): DSLConfiguration {
        return configure {

            textPref(
                title = DSLSettingsText.from(R.string.HelpSettingsFragment__version),
                summary = DSLSettingsText.from(BuildConfig.VERSION_NAME)
            )

            dividerPref()

           /* externalLinkPref(
                title = DSLSettingsText.from(R.string.HelpSettingsFragment__terms_amp_privacy_policy),
                linkId = R.string.terms_and_privacy_policy_url
            )*/
        }
    }
}
