package com.divinelink.core.network.client.test

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class HttpClientOptions(
  /**
   * Follows HTTP redirect responses if set to true.
   * Defaults to true.
   */
  val followRedirects: Boolean = true,

  /**
   * Timeout for a complete HTTP request.
   * Defaults to 30 seconds.
   */
  val requestTimeout: Duration = 30.seconds,

  /**
   * Timeout for connecting to the server.
   * Defaults to 6 seconds.
   */
  val connectTimeout: Duration = 6.seconds,

  /**
   * Timeout between receiving or writing messages.
   * Defaults to 30 seconds.
   */
  val socketTimeout: Duration = 30.seconds,

  /**
   * Policy to use for WebSocket reconnections.
   * Defaults to [SocketReconnectPolicy.ExponentialDelayReconnect].
   */
// 	val socketReconnectPolicy: SocketReconnectPolicy = SocketReconnectPolicy.ExponentialDelayReconnect(),
)
