package com.divinelink.core.data.session.model

object SessionException {

  class Unauthenticated : Exception()

  class InvalidAccountId: Exception()
}
