@file:Suppress("MagicNumber")

package com.divinelink.core.network.client

import io.ktor.client.plugins.logging.Logger
import timber.log.Timber

class HttpLogger : Logger {
  override fun log(message: String) {
    if (message.length > 2800) {
      var chunkCount = message.length / 2800
      if (chunkCount > 100) { // reduce clutter
        chunkCount = 1
      }
      var chunkMessage: String
      Timber.d("HttpClient: ")
      // integer division
      for (i in 0..chunkCount) {
        val max = 2800 * (i + 1)
        if (max >= message.length) {
          chunkMessage = message.substring(2800 * i)
          Timber.d(chunkMessage)
        } else {
          chunkMessage = message.substring(2800 * i, max)
          Timber.d(chunkMessage)
        }
      }
    } else {
      Timber.d("HttpClient:$message")
    }
  }
}
