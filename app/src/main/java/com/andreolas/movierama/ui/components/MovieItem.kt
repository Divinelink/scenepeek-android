package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.ui.components.media.MediaRatingItem
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.PopularMovieItemShape
import com.andreolas.movierama.ui.theme.RoundedShape
import com.andreolas.movierama.ui.theme.dimensions

const val MOVIE_CARD_ITEM_TAG = "MOVIE_CARD_ITEM_TAG"

@Composable
fun MediaItem(
  modifier: Modifier = Modifier,
  movie: MediaItem.Media,
  onMovieItemClick: (MediaItem.Media) -> Unit,
  onLikeMovieClick: () -> Unit,
) {
  val offset = MaterialTheme.dimensions.keyline_28

  Card(
    shape = PopularMovieItemShape,
    modifier = modifier
      .testTag(MOVIE_CARD_ITEM_TAG)
      .widthIn(max = 120.dp)
      .clip(PopularMovieItemShape)
      .clipToBounds()
      .clickable {
        onMovieItemClick(movie)
      },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
      MovieImage(
        path = movie.posterPath,
      )
      movie.isFavorite?.let { isFavorite ->
        LikeButton(
          modifier = Modifier.align(Alignment.TopStart),
          isFavorite = isFavorite,
          onClick = onLikeMovieClick,
        )
      }

      MediaRatingItem(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .offset(y = offset)
          .padding(start = 8.dp),
        rating = movie.rating,
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, bottom = 4.dp, end = 8.dp)
        .offset(y = offset)
        .height(MaterialTheme.dimensions.keyline_40),
      text = movie.name,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
      modifier = Modifier
        .offset(y = offset)
        .padding(start = 8.dp, bottom = 8.dp),
      text = movie.releaseDate,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
    )

    Spacer(modifier = Modifier.height(offset))
  }
}

@Composable
fun LikeButton(
  modifier: Modifier = Modifier,
  isFavorite: Boolean,
  transparentBackground: Boolean = false,
  onClick: () -> Unit,
) {
  val color by animateColorAsState(
    targetValue = when (isFavorite) {
      true -> colorResource(id = R.color.core_red_highlight)
      false -> colorResource(id = R.color.core_grey_55)
    },
    label = "Like button color",
  )

  val backgroundColor = when (transparentBackground) {
    true -> Color.Transparent
    false -> MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
  }

  Box(
    modifier = modifier
      .padding(4.dp)
      .clip(RoundedShape)
      .background(color = backgroundColor)
      .clickable { onClick() }
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
}

@Composable
@ExcludeFromKoverReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PopularMovieItemPreview() {
  AppTheme {
    Surface(
      modifier = Modifier
        .width(160.dp)
        .height(340.dp)
    ) {
      MediaItem(
        modifier = Modifier,
        movie = MediaItem.Media.TV(
          id = 0,
          posterPath = "original/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
          releaseDate = "2023",
          name = "Fight Club",
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
@ExcludeFromKoverReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun MovieItemPreview() {
  AppTheme {
    Surface(
      modifier = Modifier
    ) {
      MediaItem(
        modifier = Modifier,
        movie = MediaItem.Media.Movie(
          id = 0,
          posterPath = "/w200/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
          releaseDate = "2023",
          name = "Night of the Day of the Dawn of the Son of the Bride of the " +
            "Return of the Revenge of the Terror of the Attack of the Evil," +
            " Mutant, Alien, Flesh Eating, Hellbound, Zombified Living Dead",
          rating = "0.1",
          isFavorite = null,
          overview = "",
        ),
        onMovieItemClick = {},
        onLikeMovieClick = {},
      )
    }
  }
}
