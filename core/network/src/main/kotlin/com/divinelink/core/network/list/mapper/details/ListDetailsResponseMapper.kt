package com.divinelink.core.network.list.mapper.details

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.media.model.search.multi.mapper.mapToMedia

fun ListDetailsResponse.map() = ListDetails(
  name = name,
  media = results.mapToMedia(),
)
