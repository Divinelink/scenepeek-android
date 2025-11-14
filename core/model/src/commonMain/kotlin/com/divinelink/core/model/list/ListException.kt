package com.divinelink.core.model.list

sealed class ListException : Exception() {
  class ItemAlreadyExists : ListException()
  class UnexpectedError : ListException()
}
