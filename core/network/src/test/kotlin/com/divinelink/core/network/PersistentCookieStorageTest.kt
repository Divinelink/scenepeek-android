package com.divinelink.core.network

import com.divinelink.core.network.client.PersistentCookieStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import io.ktor.http.Url
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

// TODO: Fix tests here
class PersistentCookieStorageTest {

  /**
   * test the following
   * override suspend fun get(requestUrl: Url): List<Cookie> {
   *     val cookies = mutableListOf<Cookie>()
   *
   *     val storedCookie = storage.jellyseerrAuthCookie ?: return cookies
   *
   *     val cookie = stringToCookie(storedCookie)
   *     if (cookie != null && !cookie.isExpired()) {
   *       cookies.add(cookie)
   *     }
   *     return cookies
   *   }
   */
  @Test
  fun `test get cookie when cookie is null`() = runTest {
    // Given
    val cookieStorage = PersistentCookieStorage(
      FakeEncryptedPreferenceStorage(
        jellyseerrAuthCookie = null,
      ),
    )

    // When
    val cookie = cookieStorage.get(Url("https://jellyseerr.com"))

    // Then
    assertThat(cookie).isEmpty()
  }

  @Test
  fun `test get cookie when cookie is expired`() = runTest {
    // Given
    val cookieString = "connect.sid=" +
      "s%3ADUlK3iTaDISW0NKO5WkcvG87gW7e3sAO.f40uiJAPd4RQJLz9boVMFB0%2Bfmz%2FgSIZcPe57xkptRk; " +
      "Path=/; " +
      "Expires=Sat, 27 Jul 2024 21:37:07 GMT; " +
      "HttpOnly; " +
      "SameSite=Lax"

    val cookieStorage = PersistentCookieStorage(
      FakeEncryptedPreferenceStorage(
        jellyseerrAuthCookie = cookieString,
      ),
    )

    // When
    val cookie = cookieStorage.get(Url("https://jellyseerr.com"))

    // Then

    assertThat(cookie).isEmpty()
  }

  @Test
  fun `test get cookie when cookie is not expired`() = runTest {
    // Given
    /**
     *    cookie.name,
     *     cookie.value,
     *     cookie.domain,
     *     cookie.path,
     *     cookie.expires?.timestamp,
     *     cookie.maxAge.toString(),
     *     cookie.secure.toString(),
     *     cookie.httpOnly.toString(),
     */
    val initialCookie = "connect.sid; " +
      "s%3ADUlK3iTaDISW0NKO5WkcvG87gW7e3sAO.f40uiJAPd4RQJLz9boVMFB0%2Bfmz%2FgSIZcPe57xkptRk; " +
      "null; " +
      "Path=/; " +
      "Expires=Sat, 27 Jul 2024 21:37:07 GMT; " +
      "0; " +
      "HttpOnly; " +
      "SameSite=Lax"
  }
}
