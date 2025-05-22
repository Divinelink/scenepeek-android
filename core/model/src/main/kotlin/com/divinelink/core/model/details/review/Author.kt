package com.divinelink.core.model.details.review

data class Author(
  val name: String,
  val avatarPath: String? = null,
  val username: String,
) {
  val avatarUrl
    get() = avatarPath?.let { "https://image.tmdb.org/t/p/w500$it" }
  val displayName: String
    get() = name.ifBlank { username }
}
