package com.divinelink.core.commons.exception

class InvalidStatusException(
  val status: Int,
  cause: Throwable? = null,
) : ApiClientException("Invalid HTTP status in response: $status", cause)
