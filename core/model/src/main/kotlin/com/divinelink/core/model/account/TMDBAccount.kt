package com.divinelink.core.model.account

sealed interface TMDBAccount {
  data object NotLoggedIn : TMDBAccount
  data class LoggedIn(val accountDetails: AccountDetails) : TMDBAccount
}
