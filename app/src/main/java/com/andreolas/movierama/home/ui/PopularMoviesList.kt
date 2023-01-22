package com.andreolas.movierama.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.components.PopularMovieItem
import com.andreolas.movierama.ui.theme.AppTheme

@Composable
fun PopularMoviesList(
    modifier: Modifier = Modifier,
    movies: List<PopularMovie>,
    onMovieClicked: (PopularMovie) -> Unit,
    onMarkAsFavoriteClicked: (PopularMovie) -> Unit,
    //    state: LazyListState = LazyListState(),
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(
                start = 8.dp,
                end = 8.dp
            ),
        columns = GridCells.Adaptive(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(movies) { movie ->
            PopularMovieItem(
                movie = movie,
                onMovieItemClick = { onMovieClicked(movie) },
                onLikeMovieClick = { onMarkAsFavoriteClicked(movie) }
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ListBeansScreenPreview() {
    @Suppress("MagicNumber")
    val movies = (1..10).map { index ->
        PopularMovie(
            id = index,
            posterPath = "original/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
            releaseDate = (2000 + index).toString(),
            title = "Fight Club $index",
            isFavorite = index % 2 == 0,
            rating = index.toString(),
        )
    }.toMutableList()

    AppTheme {
        Surface {
            PopularMoviesList(
                movies = movies,
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {}

            )
        }
    }
}
