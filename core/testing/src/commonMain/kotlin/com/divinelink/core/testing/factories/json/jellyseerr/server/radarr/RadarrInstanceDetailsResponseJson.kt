package com.divinelink.core.testing.factories.json.jellyseerr.server.radarr

object RadarrInstanceDetailsResponseJson {

  val default = """
    {
      "server": {
        "id": 0,
        "name": "Radarr",
        "is4k": false,
        "isDefault": true,
        "activeDirectory": "/data/movies",
        "activeProfileId": 6,
        "activeTags": []
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
          "id": 5,
          "name": "Ultra-HD"
        },
        {
          "id": 6,
          "name": "HD - 720p/1080p"
        }
      ],
      "rootFolders": [
        {
          "id": 1,
          "freeSpace": 1953237553152,
          "path": "/data/movies"
        }
      ],
      "tags": []
    }
  """.trimIndent()
}
