package com.andreolas.movierama.ui.components.details.cast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.details.domain.model.Actor
import com.andreolas.movierama.ui.components.MovieImage
import com.andreolas.movierama.ui.theme.PopularMovieItemShape

@Composable
fun CrewItemCard(
    modifier: Modifier = Modifier,
    actor: Actor,
) {
    Card(
        shape = PopularMovieItemShape,
        modifier = Modifier
            .clip(PopularMovieItemShape)
            .clipToBounds()
            .clickable {
                //                onMovieItemClick()
            },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            MovieImage(path = actor.profilePath)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = 4.dp, end = 8.dp)
                .height(40.dp),
            text = actor.name,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            text = actor.character,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
        )
    }
}
