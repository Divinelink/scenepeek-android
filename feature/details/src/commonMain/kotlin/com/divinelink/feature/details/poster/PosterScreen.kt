package com.divinelink.feature.details.poster

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.SharedElementKeys
import com.divinelink.core.ui.coil.PosterImage
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.conditional

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.PosterScreen(
  path: String,
  onNavigate: (Navigation) -> Unit,
) {
  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    topBar = {
      AppTopAppBar(
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = Color.Transparent,
          containerColor = Color.Transparent,
          navigationIconContentColor = Color.Transparent,
          titleContentColor = Color.Transparent,
          actionIconContentColor = Color.Transparent,
          subtitleContentColor = Color.Transparent,
        ),
        text = UIText.StringText(""),
        onNavigateUp = { onNavigate(Navigation.Back) },
        progress = 0f,
      )
    },
    content = { paddingValues ->
      FullscreenPoster(
        path = path,
        onClick = { onNavigate(Navigation.Back) },
        visibilityScope = this,
      )
    },
  )
}

@Composable
fun SharedTransitionScope.FullscreenPoster(
  modifier: Modifier = Modifier,
  path: String,
  onClick: () -> Unit,
  visibilityScope: AnimatedVisibilityScope,
) {
  val windowInfo = LocalWindowInfo.current
  val isLandscape = windowInfo.containerSize.width > windowInfo.containerSize.height

  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    PosterImage(
      modifier = modifier
        .sharedElement(
          sharedContentState = rememberSharedContentState(
            SharedElementKeys.MediaPoster(path)
          ),
          animatedVisibilityScope = visibilityScope,
        )
        .align(Alignment.Center)
        .conditional(
          condition = isLandscape,
          ifTrue = { fillMaxHeight() },
          ifFalse = { fillMaxWidth() },
        ),
      quality = ImageQuality.ORIGINAL,
      onClick = { onClick() },
      path = path,
      contentScale = if (isLandscape) {
        ContentScale.FillHeight
      } else {
        ContentScale.FillWidth
      },
    )
  }
}
