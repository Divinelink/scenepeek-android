package com.andreolas.movierama.ui.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.andreolas.movierama.R
import com.andreolas.movierama.base.communication.ApiConstants

@Composable
fun MovieImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    errorPlaceHolder: Painter = painterResource(R.drawable.ic_movie_placeholder),
    path: String?,
) {
    AsyncImage(
        modifier = modifier
            .heightIn(min = 160.dp)
            .widthIn(min = 120.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .data(ApiConstants.TMDB_IMAGE_URL + path)
            .crossfade(true)
            .build(),
        error = errorPlaceHolder,
        contentDescription = stringResource(id = R.string.movie_image_placeholder),
        contentScale = contentScale,
    )
}
