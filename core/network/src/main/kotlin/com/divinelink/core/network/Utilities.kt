package com.divinelink.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

sealed class Resource<T> {
  data class Success<T>(val data: T) : Resource<T>()
  data class Error<T>(
    val error: Throwable,
    val data: T? = null,
  ) : Resource<T>()

  data class Loading<T>(val data: T? = null) : Resource<T>()
}

// Network Bound Resource for offline-first pattern
inline fun <ResultType, RequestType> networkBoundResource(
  crossinline query: () -> Flow<ResultType>,
  crossinline fetch: suspend () -> RequestType,
  crossinline saveFetchResult: suspend (RequestType) -> Unit,
  crossinline shouldFetch: (ResultType) -> Boolean = { true },
) = flow {
  // First, emit data from local database
  val data = query().first()
  emit(Resource.Loading(data))

  // Check if we should fetch from network
  if (shouldFetch(data)) {
    try {
      // Fetch from network
      val apiResponse = fetch()
      // Save to database
      saveFetchResult(apiResponse)
      // Emit fresh data from database
      query().collect { newData ->
        emit(Resource.Success(newData))
      }
    } catch (throwable: Throwable) {
      // Emit error with cached data
      query().collect { cachedData ->
        emit(Resource.Error(throwable, cachedData))
      }
    }
  } else {
    // Just emit the cached data as success
    query().collect { cachedData ->
      emit(Resource.Success(cachedData))
    }
  }
}
