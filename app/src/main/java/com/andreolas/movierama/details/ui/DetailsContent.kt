package com.andreolas.movierama.details.ui

import android.content.res.Configuration
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.theme.AppTheme

@Composable
fun DetailsContent(
    viewState: DetailsViewState,
    modifier: Modifier = Modifier,
) {
    // TODO
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
@Suppress("UnusedPrivateMember")
@ExcludeFromJacocoGeneratedReport
private fun DetailsContentPreview(
    @PreviewParameter(DetailsViewStateProvider::class)
    viewState: DetailsViewState,
) {
    AppTheme {
        Surface {
            DetailsContent(
                viewState = viewState,
            )
        }
    }
}

class DetailsViewStateProvider : PreviewParameterProvider<DetailsViewState> {

    override val values: Sequence<DetailsViewState>
        get() {
            val movieDetails = MovieDetails(
                id = 1123,
                posterPath = "123456",
                releaseDate = "2022",
                title = "Flight Club",
                rating = "9.5",
                isFavorite = false,
                overview = "This movie is good.",
                director = Director(id = 123443321, name = "Forest Gump", profilePath = "BoxOfChocolates.jpg"),
                cast = listOf(
                    Actor(
                        id = 10,
                        name = "Jack",
                        profilePath = "AllWorkAndNoPlay.jpg",
                        character = "HelloJohnny",
                        order = 0
                    ),
                    Actor(
                        id = 20,
                        name = "Nicholson",
                        profilePath = "Cuckoo.jpg",
                        character = "McMurphy",
                        order = 1
                    ),
                ),
                genres = listOf("Thriller", "Drama", "Comedy"),
            )

            return sequenceOf(
                DetailsViewState.Initial,

                DetailsViewState.Completed(
                    movieDetails = movieDetails,
                ),

                DetailsViewState.Error(
                    error = UIText.StringText("Something went wrong.")
                ),
            )
        }
}
