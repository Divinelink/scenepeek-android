package com.divinelink.core.network

object Routes {
  object TMDb {
    const val HOST = "api.themoviedb.org"
    const val TMDB_AUTH_SCHEME = "scenepeek"
    const val AUTH_REDIRECT_URL = "$TMDB_AUTH_SCHEME://auth/redirect"
    const val V3 = "3"
    const val V4 = "4"
  }

  object OMDb {
    const val HOST = "omdbapi.com/"
  }

  object Trakt {
    const val HOST = "api.trakt.tv"
  }

  object Awards {
    const val BRANCH = "main"
    const val HOST = "raw.githubusercontent.com/ScenePeek/tmdb-awards/refs/heads/$BRANCH"
  }
}
