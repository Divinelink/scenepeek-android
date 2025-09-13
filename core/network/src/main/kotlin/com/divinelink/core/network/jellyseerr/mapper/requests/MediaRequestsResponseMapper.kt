package com.divinelink.core.network.jellyseerr.mapper.requests

import com.divinelink.core.model.jellyseerr.JellyseerrRequests
import com.divinelink.core.model.jellyseerr.PageInfo
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.requests.MediaRequestsResponse

fun MediaRequestsResponse.map() = JellyseerrRequests(
  pageInfo = PageInfo(
    pages = pageInfo.pages,
    pageSize = pageInfo.pageSize,
    results = pageInfo.results,
    page = pageInfo.page,
  ),
  results = this.results.map(),
)
