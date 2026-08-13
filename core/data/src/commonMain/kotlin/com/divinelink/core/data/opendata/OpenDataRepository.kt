package com.divinelink.core.data.opendata

import com.divinelink.core.model.locale.LocaleCode

interface OpenDataRepository {

  suspend fun getWikiTitles(wikidataId: String): Result<Map<LocaleCode, String>>
}
