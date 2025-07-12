package com.divinelink.core.model.list

sealed interface AddToListResult {
  data object Success : AddToListResult
  sealed interface Failure : AddToListResult {
    data object ItemAlreadyExists : Failure
    data object UnexpectedError : Failure
  }
}
