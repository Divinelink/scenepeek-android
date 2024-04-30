package com.andreolas.movierama.session.model

data class RequestToken(
  val token: String,
  val expiresAt: String
) {
  companion object {
    const val APPROVED_URL = "approved=true"
  }
}

val String.tokenIsApproved: Boolean
  get() = contains(RequestToken.APPROVED_URL)
