package com.divinelink.core.network.omdb.service

import com.divinelink.core.network.omdb.model.OMDbResponseApi
import kotlinx.coroutines.flow.Flow

interface OMDbService {
  fun fetchImdbDetails(imdbId: String): Flow<OMDbResponseApi>
}
