package com.divinelink.core.network.app.mapper

import com.divinelink.core.model.config.Config
import com.divinelink.core.model.config.ConfigMessage
import com.divinelink.core.network.app.model.ConfigMessageResponse
import com.divinelink.core.network.app.model.ConfigResponse

fun ConfigResponse.map() = Config(
  messages = messages?.map() ?: emptyList(),
)

fun List<ConfigMessageResponse>.map() = map { it.map() }

fun ConfigMessageResponse.map() = ConfigMessage(
  id = id,
  content = content,
  visible = visible,
  dismissable = dismissable,
)
