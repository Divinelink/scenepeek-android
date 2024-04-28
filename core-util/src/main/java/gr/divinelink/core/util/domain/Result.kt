package gr.divinelink.core.util.domain


/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
//val Result<*>.succeeded
//    get() = this.succeeded  && data != null

//fun <T> Result<T>.successOr(fallback: T): T {
//    return (this as? Success<T>)?.data ?: fallback
//}

val <T> Result<T>.data: T
  get() = (this.getOrThrow())
