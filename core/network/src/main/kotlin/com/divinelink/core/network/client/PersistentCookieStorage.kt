package com.divinelink.core.network.client

import com.divinelink.core.datastore.EncryptedStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import javax.inject.Inject

/**
 * A [CookiesStorage] implementation that stores cookies in an encrypted storage.
 */

class PersistentCookieStorage @Inject constructor(val storage: EncryptedStorage) : CookiesStorage {
  override suspend fun get(requestUrl: Url): List<Cookie> {
    val cookies = mutableListOf<Cookie>()

    val storedCookie = storage.jellyseerrAuthCookie ?: return cookies

    val cookie = stringToCookie(storedCookie)
    if (cookie != null && !cookie.isExpired()) {
      cookies.add(cookie)
    }
    return cookies
  }

  override suspend fun addCookie(
    requestUrl: Url,
    cookie: Cookie,
  ) {
    storage.setJellyseerrAuthCookie(cookieToString(cookie))
  }

  override fun close() {
    // No-op
  }

  private fun cookieToString(cookie: Cookie): String = listOf(
    cookie.name,
    cookie.value,
    cookie.domain,
    cookie.path,
    cookie.expires?.timestamp,
    cookie.maxAge.toString(),
    cookie.secure.toString(),
    cookie.httpOnly.toString(),
  ).joinToString("; ")

  private fun stringToCookie(cookieString: String): Cookie? {
    val parts = cookieString.split("; ")
    if (parts.size < 8) return null

    val expires = parts[4].toLongOrNull()?.let { GMTDate(it) }

    return Cookie(
      name = parts[0],
      value = parts[1],
      domain = parts[2],
      path = parts[3],
      expires = expires,
      maxAge = parts[5].toInt(),
      secure = parts[6].toBoolean(),
      httpOnly = parts[7].toBoolean(),
    )
  }

  private fun Cookie.isExpired(): Boolean = expires?.let { it < GMTDate() } ?: false
}
