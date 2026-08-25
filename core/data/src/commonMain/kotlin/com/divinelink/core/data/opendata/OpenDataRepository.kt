package com.divinelink.core.data.opendata

import com.divinelink.core.model.locale.LocaleCode
import com.divinelink.core.model.media.MediaReference

interface OpenDataRepository {

  suspend fun getWikiTitles(wikidataId: String): Result<Map<LocaleCode, String>>

  suspend fun getMediaFromLetterboxd(url: String): Result<MediaReference>
}
