package com.divinelink.core.data.details.mapper.api.reviews

import com.divinelink.core.model.details.review.Author
import com.divinelink.core.network.media.model.details.reviews.AuthorDetailsApi

fun AuthorDetailsApi.map() = Author(
  name = name,
  avatarPath = avatarPath,
  username = username,
)
