package com.andreolas.movierama.fakes.dao

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeMovieDAO {

    val mock: MovieDAO = mock()

    fun mockFetchFavoritesMovies(
        result: Flow<List<PersistableMovie>>,
    ) {
        whenever(
            mock.fetchFavoriteMovies()
        ).thenReturn(
            result
        )
    }

    suspend fun verifyInsertFavoriteMovie(
        movie: PersistableMovie,
    ) {
        verify(mock).insertFavoriteMovie(movie)
    }

    suspend fun verifyRemoveMovie(
        id: Int,
    ) {
        verify(mock).removeFavoriteMovie(id)
    }
}
