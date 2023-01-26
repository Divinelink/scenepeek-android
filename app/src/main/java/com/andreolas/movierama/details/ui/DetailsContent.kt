package com.andreolas.movierama.details.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.details.domain.model.Director
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.ui.LoadingContent
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    viewState: DetailsViewState,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )

    BottomSheetScaffold(
        sheetElevation = 32.dp,
        sheetPeekHeight = 1.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            // to do
        },
        modifier = Modifier
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewState.movie.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp,
                    ) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
            )
        }
    ) { paddingValues ->
        if (viewState is DetailsViewState.Initial) {
            LoadingContent()
        }
    }
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
                modifier = Modifier,
                viewState = viewState,
                onNavigateUp = {},
            )
        }
    }
}

class DetailsViewStateProvider : PreviewParameterProvider<DetailsViewState> {

    override val values: Sequence<DetailsViewState>
        get() {
            val popularMovie = PopularMovie(
                id = 0,
                posterPath = "",
                releaseDate = "",
                title = "Flight Club",
                rating = "",
                overview = "",
                isFavorite = false,
            )
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
                DetailsViewState.Initial(
                    movie = popularMovie,
                ),

                DetailsViewState.Completed(
                    movie = popularMovie,
                    movieDetails = movieDetails,
                ),

                DetailsViewState.Error(
                    movie = popularMovie,
                    error = UIText.StringText("Something went wrong.")
                ),
            )
        }
}
