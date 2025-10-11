package com.divinelink.core.model.exception

import kotlinx.io.IOException

sealed class AppException(message: String? = null) : IOException(message) {

  // Network Errors
  class Offline(message: String? = null) : AppException(message)
  class RequestTimeout(message: String? = null) : AppException(message)
  class ConnectionTimeout(message: String? = null) : AppException(message)
  class SocketTimeout(message: String? = null) : AppException(message)
  class UnreachableHost(message: String? = null) : AppException(message)
  class Ssl(message: String? = null) : AppException(message)
  class Serialization(message: String? = null) : AppException(message)
  //  class UnknownHost : Data()

  // Local Errors
  class DiskFull : AppException()
  class PermissionDenied : AppException()
  class FileNotFound : AppException()
  class CacheFailure : AppException()
  class Database : AppException()

  // HTTP Errors
  class Unauthorized(customMessage: String? = null) : AppException(message = customMessage) // 401
  class Forbidden(message: String? = null) : AppException(message)
  class NotFound(message: String? = null) : AppException(message)
  class Conflict(message: String? = null) : AppException(message)
  class TooManyRequests(message: String? = null) : AppException(message)
  class PayloadTooLarge(message: String? = null) : AppException(message)
  class ServerError(message: String? = null) : AppException(message)
  class BadRequest(message: String? = null) : AppException(message)

  data class Unknown(override val message: String? = null) : AppException(message = message)
}
