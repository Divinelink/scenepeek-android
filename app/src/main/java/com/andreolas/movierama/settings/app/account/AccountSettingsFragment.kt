package com.andreolas.movierama.settings.app.account

import androidx.fragment.app.viewModels
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.dsl.DSLConfiguration
import com.andreolas.movierama.settings.dsl.DSLSettingsAdapter
import com.andreolas.movierama.settings.dsl.DSLSettingsFragment
import com.andreolas.movierama.settings.dsl.DSLSettingsText
import com.andreolas.movierama.settings.dsl.configure
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSettingsFragment : DSLSettingsFragment(R.string.preferences__account) {
  private val viewModel: AccountSettingsViewModel by viewModels()

  override fun bindAdapter(adapter: DSLSettingsAdapter) {
    adapter.submitList(getConfiguration().toMappingModelList())
  }

  private fun getConfiguration(): DSLConfiguration {
    return configure {
      clickPref(
        title = DSLSettingsText.from(R.string.AccountSettingsFragment__login),
        onClick = {
          viewModel.login()
        }
      )
    }
  }
}
