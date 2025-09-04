package com.divinelink.core.testing.factories.json.jellyseerr.radarr

object RadarrInstanceResponseJson {

  val success = """
    [
      {
        "id": 0,
        "name": "Radarr",
        "is4k": false,
        "isDefault": true,
        "activeDirectory": "/data/movies",
        "activeProfileId": 6
      },
      {
        "id": 1,
        "name": "4K Radarr",
        "is4k": true,
        "isDefault": true,
        "activeDirectory": "/data/movies",
        "activeProfileId": 5
      },
      {
        "id": 2,
        "name": "Secondary Radarr",
        "is4k": false,
        "isDefault": false,
        "activeDirectory": "/data/movies",
        "activeProfileId": 2
      }
    ]
  """.trimIndent()
}
