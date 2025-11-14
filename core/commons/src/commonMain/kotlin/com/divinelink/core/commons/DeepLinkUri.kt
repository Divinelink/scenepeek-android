package com.divinelink.core.commons

// TODO Add tests
data class DeepLinkUri(
  val raw: String,
  val scheme: String? = null,
  val host: String? = null,
  val path: String? = null,
  private val queryParams: Map<String, String> = emptyMap(),
) {
  companion object {
    fun parse(uriString: String?): DeepLinkUri? {
      uriString ?: return null

      return try {
        val schemeEnd = uriString.indexOf("://")
        val scheme = if (schemeEnd > 0) uriString.substring(0, schemeEnd) else null

        val afterScheme = if (schemeEnd > 0) uriString.substring(schemeEnd + 3) else uriString
        val queryStart = afterScheme.indexOf('?')
        val fragmentStart = afterScheme.indexOf('#')

        val pathEnd = when {
          queryStart > 0 -> queryStart
          fragmentStart > 0 -> fragmentStart
          else -> afterScheme.length
        }

        val hostAndPath = afterScheme.substring(0, pathEnd)
        val pathStart = hostAndPath.indexOf('/')
        val host = if (pathStart > 0) hostAndPath.substring(0, pathStart) else hostAndPath
        val path = if (pathStart > 0) hostAndPath.substring(pathStart) else "/"

        val queryParams = if (queryStart > 0) {
          val queryEnd = if (fragmentStart > queryStart) {
            fragmentStart
          } else {
            afterScheme.length
          }

          afterScheme
            .substring(queryStart + 1, queryEnd)
            .split("&")
            .mapNotNull { param ->
              val parts = param.split("=", limit = 2)
              if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()
        } else {
          emptyMap()
        }

        DeepLinkUri(
          raw = uriString,
          scheme = scheme,
          host = host.takeIf { it.isNotEmpty() },
          path = path,
          queryParams = queryParams,
        )
      } catch (e: Exception) {
        null
      }
    }
  }
}
