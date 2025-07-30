package com.divinelink.core.model.exception

import kotlinx.io.IOException

sealed class AppException(message: String? = null) : IOException(message) {

  // Network Errors
  class Offline : AppException()
  class RequestTimeout : AppException()
  class ConnectionTimeout : AppException()
  class SocketTimeout : AppException()
  class UnreachableHost : AppException()
  class Ssl : AppException()
  class Serialization : AppException()
  //  class UnknownHost : Data()

  // Local Errors
  class DiskFull : AppException()
  class PermissionDenied : AppException()
  class FileNotFound : AppException()
  class CacheFailure : AppException()
  class Database : AppException()

  // HTTP Errors
  class Unauthorized(customMessage: String?) : AppException(message = customMessage) // 401
  class Forbidden : AppException()
  class NotFound : AppException()
  class Conflict : AppException()
  class TooManyRequests : AppException()
  class PayloadTooLarge : AppException()
  class ServerError : AppException()
  class BadRequest : AppException()

  class Unknown(override val message: String? = null) : AppException(message = message)
}
