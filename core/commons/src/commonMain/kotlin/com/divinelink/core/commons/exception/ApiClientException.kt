package com.divinelink.core.commons.exception

open class ApiClientException(
  message: String? = null,
  cause: Throwable? = null,
) : Exception(message, cause)
