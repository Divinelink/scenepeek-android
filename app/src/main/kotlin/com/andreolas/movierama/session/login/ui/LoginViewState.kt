package com.andreolas.movierama.session.login.ui

data class LoginViewState(
  val token: String,
  val navigateBack: Boolean
) {
  val redirectUrl = "http://success-url/handled"
  val url = "https://www.themoviedb.org/authenticate/$token?redirect_to=$redirectUrl"

  companion object {
    fun initial(token: String) = LoginViewState(
      token = token,
      navigateBack = false
    )
  }
}
