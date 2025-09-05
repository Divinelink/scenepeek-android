package com.divinelink.core.testing.factories.json.jellyseerr.server.sonarr

object SonarrInstanceResponseJson {

  val success = """
    [
      {
        "id": 0,
        "name": "Sonarr",
        "is4k": false,
        "isDefault": true,
        "activeDirectory": "/data/tv",
        "activeProfileId": 6,
        "activeAnimeProfileId": 6,
        "activeAnimeDirectory": "/data/anime",
        "activeLanguageProfileId": 1,
        "activeAnimeLanguageProfileId": 1,
        "activeTags": []
      },
      {
        "id": 1,
        "name": "Sonarr 4K",
        "is4k": true,
        "isDefault": true,
        "activeDirectory": "/data/tv",
        "activeProfileId": 6,
        "activeTags": []
      }
    ]
  """.trimIndent()
}
