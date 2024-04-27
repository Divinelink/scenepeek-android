package com.andreolas.movierama.session

data class RequestToken(
  val token: String
) {
  val url = "https://www.themoviedb.org/authenticate/$token"
}
