@file:Suppress("LongMethod")

package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.Movie
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.PopularMovieItemShape

const val MOVIE_CARD_ITEM_TAG = "MOVIE_CARD_ITEM_TAG"

@Composable
fun MovieItem(
  modifier: Modifier = Modifier,
  movie: Movie,
  onMovieItemClick: (Movie) -> Unit,
  onLikeMovieClick: () -> Unit,
) {
  Card(
    shape = PopularMovieItemShape,
    modifier = modifier
      .testTag(MOVIE_CARD_ITEM_TAG)
      .widthIn(max = 140.dp)
      .clip(PopularMovieItemShape)
      .clipToBounds()
      .clickable {
        onMovieItemClick(movie)
      },
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
      MovieImage(
        contentScale = ContentScale.Crop,
        modifier = Modifier.height(180.dp),
        path = movie.posterPath,
      )
      movie.isFavorite?.let {
        Column(
          modifier = Modifier
            .align(Alignment.TopStart)
            .clip(RoundedCornerShape(bottomEnd = 8.dp))
            .clickable { onLikeMovieClick() }
        ) {
          LikeButton(
            isFavorite = movie.isFavorite,
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(4.dp))
    Rating(
      modifier = Modifier.padding(start = 8.dp),
      rating = movie.rating,
    )

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, bottom = 4.dp, end = 8.dp)
        .height(40.dp),
      text = movie.title,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
      modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
      text = movie.releaseDate,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
    )
  }
}

@Composable
fun LikeButton(
  modifier: Modifier = Modifier,
  isFavorite: Boolean,
  transparentBackground: Boolean = false,
) {
  val color by animateColorAsState(
    targetValue = when (isFavorite) {
      true -> colorResource(id = R.color.core_red_highlight)
      false -> colorResource(id = R.color.core_grey_55)
    }
  )

  val backgroundColor = when (transparentBackground) {
    true -> Color.Transparent
    false -> MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
  }

  Box(
    modifier = modifier
      .background(color = backgroundColor)
      .size(44.dp)
  ) {
    Crossfade(
      modifier = Modifier.align(Alignment.Center),
      targetState = isFavorite,
      label = "Like button",
    ) { favorite ->
      val image = when (favorite) {
        true -> Icons.Default.Favorite
        false -> Icons.Default.FavoriteBorder
      }
      Icon(
        modifier = Modifier.size(32.dp),
        imageVector = image,
        tint = color,
        contentDescription = stringResource(R.string.mark_as_favorite_button_content_description),
      )
    }
  }
//  }mark_as_favorite_button_content_description
}

@Composable
@Suppress("MagicNumber")
fun Rating(
  modifier: Modifier = Modifier,
  rating: String,
) {
  val sanitizedRating = if (rating.endsWith(".0")) {
    rating.substring(0, rating.length - 2)
  } else {
    rating
  }

  val color = when (rating.toDouble()) {
    in 0.0..3.0 -> Color.Red
    in 3.0..6.0 -> Color.Yellow
    in 6.0..10.0 -> Color.Green
    else -> Color.White
  }
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .padding(top = 4.dp, bottom = 4.dp)
  ) {
    Canvas(
      modifier = Modifier.size(36.dp)
    ) {
      drawArc(
        color = color.copy(alpha = 0.2f),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
        style = Stroke(
          width = 4.dp.toPx(),
          miter = 4f,
        )
      )
      drawArc(
        color = color,
        startAngle = 270f,
        sweepAngle = (100f / 10f * rating.toDouble() * 3.6f).toFloat(),
        useCenter = false,
        style = Stroke(
          width = 4.dp.toPx(),
          miter = 2f,
          cap = StrokeCap.Round,
        )
      )
    }

    Text(
      text = sanitizedRating,
      style = MaterialTheme.typography.labelMedium,
      textAlign = TextAlign.Center,
    )
  }
}

@Composable
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PopularMovieItemPreview() {
  AppTheme {
    Surface(
      modifier = Modifier
        .width(160.dp)
        .height(340.dp)
    ) {
      MovieItem(
        modifier = Modifier,
        movie = Movie(
          id = 0,
          posterPath = "original/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
          releaseDate = "2023",
          title = "Fight Club",
          rating = "8.8",
          isFavorite = true,
          overview = "",
        ),
        onMovieItemClick = {},
        onLikeMovieClick = {},
      )
    }
  }
}

@Composable
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MovieItemPreview() {
  AppTheme {
    Surface(
      modifier = Modifier
    ) {
      MovieItem(
        modifier = Modifier,
        movie = Movie(
          id = 0,
          posterPath = "/w200/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
          releaseDate = "2023",
          title = "Night of the Day of the Dawn of the Son of the Bride of the " +
              "Return of the Revenge of the Terror of the Attack of the Evil," +
              " Mutant, Alien, Flesh Eating, Hellbound, Zombified Living Dead",
          rating = "5.0",
          isFavorite = null,
          overview = "",
        ),
        onMovieItemClick = {},
        onLikeMovieClick = {},
      )
    }
  }
}
