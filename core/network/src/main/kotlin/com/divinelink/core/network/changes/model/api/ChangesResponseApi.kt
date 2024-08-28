package com.divinelink.core.network.changes.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ChangesResponseApi(val changes: List<ChangeApi>)
