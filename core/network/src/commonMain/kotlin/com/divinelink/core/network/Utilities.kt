@file:Suppress("TooGenericExceptionCaught")

package com.divinelink.core.network

import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.media.model.details.watchlist.SubmitOnAccountResponse
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.io.IOException

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

@Suppress("UNCHECKED_CAST", "ReturnCount")
internal suspend inline fun <T : Any> runCatchingWithNetworkRetry(
  times: Int = 3,
  initialDelay: Long = 100, // 0.1 second
  maxDelay: Long = 5000, // 5 seconds
  factor: Double = 2.0,
  block: () -> Any,
): Result<T> {
  var currentDelay = initialDelay

  repeat(times) { retry ->
    try {
      when (val response = block()) {
        is SubmitOnAccountResponse -> {
          if (response.success) {
            return Result.success(response as T)
          } else if (response.statusCode == 34) {
            if (retry == times - 1) {
              return Result.failure(
                Exception("Resource not found after $times attempts: ${response.statusMessage}"),
              )
            }
          } else {
            return Result.failure(
              Exception(
                "Request failed with status ${response.statusCode}: ${response.statusMessage}",
              ),
            )
          }
        }
        is AddToListResponse -> if (response.results.any {
            it.error?.any { error -> error == AddToListResponse.MEDIA_IS_REQUIRED } == true
          }
        ) {
          if (retry == times - 1) {
            return Result.failure(
              Exception("Resource not found after $times attempts"),
            )
          }
        } else {
          return Result.success(response as T)
        }
        else -> return Result.success(response as T)
      }
    } catch (e: IOException) {
      if (retry == times - 1) {
        return Result.failure(e)
      }
    } catch (e: ResponseException) {
      return Result.failure(e)
    }

    if (retry != times - 1) {
      delay(currentDelay)
      currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
  }

  return Result.failure(
    Exception("Request failed after $times attempts."),
  )
}
