package com.andreolas.movierama.ui.components.details.similar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.R
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.home.domain.model.Movie
import com.andreolas.movierama.home.domain.model.toMovie
import com.andreolas.movierama.ui.components.MovieItem
import com.andreolas.movierama.ui.theme.ListPaddingValues

@Composable
fun SimilarMoviesList(
    movies: List<SimilarMovie>,
    onSimilarMovieClicked: (Movie) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.details__more_like_this),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = ListPaddingValues,
        ) {
            items(
                items = movies,
                key = {
                    it.id
                }
            ) { similarMovie ->

                MovieItem(
                    movie = similarMovie.toMovie(),
                    onMovieItemClick = onSimilarMovieClicked,
                    onLikeMovieClick = {},
                )
            }
        }
    }
}
