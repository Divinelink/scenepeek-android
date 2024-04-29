package com.andreolas.movierama.session

data class RequestToken(
  val token: String
) {
  val redirectUrl = "http://success-url/approved"
  val url = "https://www.themoviedb.org/authenticate/$token?redirect_to=$redirectUrl"
}
