package com.divinelink.core.network.list.mapper.add

import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.network.list.model.add.AddToListResponse

fun AddToListResponse.map(): AddToListResult = when {
  results.any {
    it.error?.contains(AddToListResponse.ALREADY_EXISTS_ERROR) == true
  } -> AddToListResult.Failure.ItemAlreadyExists
  results.any { it.success.not() } -> AddToListResult.Failure.UnexpectedError
  else -> AddToListResult.Success
}
