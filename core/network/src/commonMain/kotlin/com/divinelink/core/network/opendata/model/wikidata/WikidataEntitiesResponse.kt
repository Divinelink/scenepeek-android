package com.divinelink.core.network.opendata.model.wikidata

import kotlinx.serialization.Serializable

@Serializable
data class WikidataEntitiesResponse(
  val entities: Map<String, WikidataEntityResponse>,
  val success: Int,
)

@Serializable
data class WikidataEntityResponse(
  val type: String,
  val id: String,
  val sitelinks: Map<String, SiteLinkResponse>,
)

@Serializable
data class SiteLinkResponse(
  val site: String,
  val title: String,
  val badges: List<String> = emptyList(),
)
