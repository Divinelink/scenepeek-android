package com.divinelink.feature.tmdb.auth

data class TMDBAuthUiState(
  val webViewFallback: Boolean,
  val url: String,
) {
  companion object {
    val initial = TMDBAuthUiState(
      webViewFallback = false,
      url = "",
    )
  }
}
