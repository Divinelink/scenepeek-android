package com.divinelink.core.model.exception

object SessionException {
  class Unauthenticated : Exception("Unauthenticated")
  class RequestTokenNotFound : Exception("Request token not found")
}
