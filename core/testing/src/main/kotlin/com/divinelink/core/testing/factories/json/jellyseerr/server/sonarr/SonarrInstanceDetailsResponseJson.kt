package com.divinelink.core.testing.factories.json.jellyseerr.server.sonarr

object SonarrInstanceDetailsResponseJson {

  val default = """
    {
      "server": {
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
        "activeTags": [],
        "activeAnimeTags": []
      },
      "profiles": [
        {
          "id": 1,
          "name": "Any"
        },
        {
          "id": 2,
          "name": "SD"
        },
        {
          "id": 3,
          "name": "HD-720p"
        },
        {
          "id": 4,
          "name": "HD-1080p"
        },
        {
          "id": 6,
          "name": "HD - 720p/1080p"
        }
      ],
      "rootFolders": [
        {
          "id": 5,
          "freeSpace": 1953233944576,
          "path": "/data/tv"
        },
        {
          "id": 6,
          "freeSpace": 1953233944576,
          "path": "/data/anime"
        }
      ],
      "languageProfiles": null,
      "tags": []
    }
  """.trimIndent()
}
