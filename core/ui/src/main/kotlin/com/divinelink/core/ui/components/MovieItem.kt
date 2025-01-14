package com.divinelink.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.PopularMovieItemShape
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.FavoriteButton
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.rating.TMDBRatingItem

const val MOVIE_CARD_ITEM_TAG = "MOVIE_CARD_ITEM_TAG"

@Composable
fun MediaItem(
  modifier: Modifier = Modifier,
  media: MediaItem.Media,
  subtitle: String? = null,
  onMediaItemClick: (MediaItem.Media) -> Unit,
  onLikeMediaClick: () -> Unit,
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
        onMediaItemClick(media)
      },
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    ) {
      MovieImage(
        path = media.posterPath,
      )
      media.isFavorite?.let { isFavorite ->
        FavoriteButton(
          modifier = Modifier.align(Alignment.TopStart),
          isFavorite = isFavorite,
          onClick = onLikeMediaClick,
        )
      }

      TMDBRatingItem(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .offset(y = offset)
          .padding(start = MaterialTheme.dimensions.keyline_8),
        rating = media.voteAverage,
        voteCount = media.voteCount,
      )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_4))

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_8)
        .offset(y = offset)
        .height(MaterialTheme.dimensions.keyline_58),
      text = media.name,
      maxLines = 3,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurface,
    )

    subtitle?.let {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .offset(y = offset)
          .padding(top = MaterialTheme.dimensions.keyline_4)
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        text = subtitle,
        maxLines = 1,
        style = MaterialTheme.typography.bodySmall,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface,
      )
    }

    Text(
      modifier = Modifier
        .offset(y = offset)
        .padding(
          vertical = MaterialTheme.dimensions.keyline_4,
          horizontal = MaterialTheme.dimensions.keyline_8,
        ),
      text = media.releaseDate,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.80f),
    )

    Spacer(modifier = Modifier.height(offset))
  }
}

@Composable
@Previews
fun PopularMovieItemPreview() {
  AppTheme {
    Surface(
      modifier = Modifier
        .width(160.dp)
        .height(340.dp),
    ) {
      MediaItem(
        modifier = Modifier,
        media = MediaItem.Media.TV(
          id = 0,
          posterPath = "original/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
          releaseDate = "2023",
          name = "Fight Club",
          voteAverage = 8.8,
          voteCount = 1000,
          isFavorite = true,
          overview = "",
        ),
        onMediaItemClick = {},
        onLikeMediaClick = {},
      )
    }
  }
}

@Composable
@Previews
fun MovieItemPreview() {
  AppTheme {
    Surface(
      modifier = Modifier,
    ) {
      MediaItem(
        modifier = Modifier,
        media = MediaItem.Media.Movie(
          id = 0,
          posterPath = "/w200/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
          releaseDate = "2023",
          name = "Night of the Day of the Dawn of the Son of the Bride of the " +
            "Return of the Revenge of the Terror of the Attack of the Evil," +
            " Mutant, Alien, Flesh Eating, Hellbound, Zombified Living Dead",
          voteAverage = 0.1,
          voteCount = 1000,
          isFavorite = null,
          overview = "",
        ),
        subtitle = "Matthew Walkers",
        onMediaItemClick = {},
        onLikeMediaClick = {},
      )
    }
  }
}
