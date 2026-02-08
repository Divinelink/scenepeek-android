package com.divinelink.scenepeek.feature.episode

@Previews
@Composable
fun EpisodeContentScreenshots(
  @PreviewParameter(EpisodeUiStateParameterProvider::class) uiState: EpisodeUiState,
) {
  EpisodeContentPreview(uiState)
}
