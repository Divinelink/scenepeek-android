package com.divinelink.core.model.exception

object SessionException {

  class Unauthenticated : Exception()

  class InvalidAccountId : Exception()
}
