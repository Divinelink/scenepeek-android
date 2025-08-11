package com.divinelink.feature.details.media.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.jellyseerr.model.JellyseerrRequestParams
import com.divinelink.core.domain.MarkAsFavoriteUseCase
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.domain.details.media.FetchAllRatingsUseCase
import com.divinelink.core.domain.jellyseerr.DeleteMediaParameters
import com.divinelink.core.domain.jellyseerr.DeleteMediaUseCase
import com.divinelink.core.domain.jellyseerr.DeleteRequestParameters
import com.divinelink.core.domain.jellyseerr.DeleteRequestUseCase
import com.divinelink.core.domain.jellyseerr.RequestMediaUseCase
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.model.details.DetailActionItem
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.canBeRequested
import com.divinelink.core.model.details.clearSeasonsStatus
import com.divinelink.core.model.details.externalUrl
import com.divinelink.core.model.details.isAvailable
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.DetailsForm
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.details.R
import com.divinelink.feature.details.media.usecase.AddToWatchlistParameters
import com.divinelink.feature.details.media.usecase.AddToWatchlistUseCase
import com.divinelink.feature.details.media.usecase.DeleteRatingParameters
import com.divinelink.feature.details.media.usecase.DeleteRatingUseCase
import com.divinelink.feature.details.media.usecase.GetMediaDetailsUseCase
import com.divinelink.feature.details.media.usecase.SubmitRatingParameters
import com.divinelink.feature.details.media.usecase.SubmitRatingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import com.divinelink.core.ui.R as uiR

class DetailsViewModel(
  getMediaDetailsUseCase: GetMediaDetailsUseCase,
  private val fetchAllRatingsUseCase: FetchAllRatingsUseCase,
  private val onMarkAsFavoriteUseCase: MarkAsFavoriteUseCase,
  private val submitRatingUseCase: SubmitRatingUseCase,
  private val deleteRatingUseCase: DeleteRatingUseCase,
  private val addToWatchlistUseCase: AddToWatchlistUseCase,
  private val requestMediaUseCase: RequestMediaUseCase,
  private val deleteRequestUseCase: DeleteRequestUseCase,
  private val spoilersObfuscationUseCase: SpoilersObfuscationUseCase,
  private val deleteMediaUseCase: DeleteMediaUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val route: DetailsRoute = DetailsRoute(
    id = savedStateHandle.get<Int>("id") ?: -1,
    mediaType = savedStateHandle.get<MediaType>("mediaType") ?: MediaType.UNKNOWN,
    isFavorite = savedStateHandle.get<Boolean>("isFavorite") ?: false,
  )

  private val _viewState: MutableStateFlow<DetailsViewState> = MutableStateFlow(
    value = DetailsViewState(
      mediaId = route.id,
      mediaType = route.mediaType,
      isLoading = true,
      tabs = when (route.mediaType) {
        MediaType.TV -> TvTab.entries
        MediaType.MOVIE -> MovieTab.entries
        else -> emptyList()
      },
      forms = when (route.mediaType) {
        MediaType.TV -> TvTab.entries.associate { tab ->
          tab.order to when (tab) {
            TvTab.About -> DetailsForm.Loading
            TvTab.Seasons -> DetailsForm.Loading
            TvTab.Cast -> DetailsForm.Loading
            TvTab.Recommendations -> DetailsForm.Loading
            TvTab.Reviews -> DetailsForm.Loading
          }
        }
        MediaType.MOVIE -> MovieTab.entries.associate { tab ->
          tab.order to when (tab) {
            MovieTab.About -> DetailsForm.Loading
            MovieTab.Cast -> DetailsForm.Loading
            MovieTab.Recommendations -> DetailsForm.Loading
            MovieTab.Reviews -> DetailsForm.Loading
          }
        }
        else -> emptyMap()
      },
    ),
  )
  val viewState: StateFlow<DetailsViewState> = _viewState.asStateFlow()

  private val _openUrlTab = Channel<String>()
  val openUrlTab: Flow<String> = _openUrlTab.receiveAsFlow()

  private val _navigateToJellyseerrAuth = Channel<Unit>()
  val navigateToJellyseerrAuth: Flow<Unit> = _navigateToJellyseerrAuth.receiveAsFlow()

  fun onMarkAsFavorite() {
    viewModelScope.launch {
      viewState.value.mediaItem?.let { mediaItem ->
        onMarkAsFavoriteUseCase(mediaItem).onSuccess {
          _viewState.update { viewState ->
            viewState.copy(
              mediaDetails = viewState.mediaDetails?.copy(
                isFavorite = !viewState.mediaDetails.isFavorite,
              ),
            )
          }
        }
      }
    }
  }

  init {
    val requestApi = when (viewState.value.mediaType) {
      MediaType.TV -> MediaRequestApi.TV(route.id)
      MediaType.MOVIE -> MediaRequestApi.Movie(route.id)
      MediaType.PERSON -> MediaRequestApi.Unknown
      MediaType.UNKNOWN -> MediaRequestApi.Unknown
    }

    getMediaDetailsUseCase(parameters = requestApi)
      .onEach { result ->
        result.onSuccess {
          _viewState.update { viewState ->
            when (result.data) {
              is MediaDetailsResult.DetailsSuccess -> {
                val data = result.data as MediaDetailsResult.DetailsSuccess

                if (data.mediaDetails is Movie) {
                  val aboutOrder = MovieTab.About.order
                  val castOrder = MovieTab.Cast.order
                  val updatedForms = viewState.forms.toMutableMap().apply {
                    this[aboutOrder] = DetailsForm.Content(getAboutDetailsData(data))
                    this[castOrder] = DetailsForm.Content(
                      DetailsData.Cast(
                        isTv = false,
                        items = data.mediaDetails.cast,
                      ),
                    )
                  }

                  viewState.copy(
                    isLoading = false,
                    forms = updatedForms,
                    mediaDetails = data.mediaDetails,
                    ratingSource = data.ratingSource,
                  )
                } else {
                  data.mediaDetails as TV
                  val aboutOrder = TvTab.About.order
                  val seasonsTabOrder = TvTab.Seasons.order
                  val updatedForms = viewState.forms.toMutableMap().apply {
                    this[aboutOrder] = DetailsForm.Content(getAboutDetailsData(data))
                    this[seasonsTabOrder] = DetailsForm.Content(
                      DetailsData.Seasons(data.mediaDetails.seasons),
                    )
                  }

                  viewState.copy(
                    isLoading = false,
                    forms = updatedForms,
                    mediaDetails = data.mediaDetails,
                    ratingSource = data.ratingSource,
                  )
                }
              }

              is MediaDetailsResult.RatingSuccess -> viewState.copy(
                mediaDetails = viewState.mediaDetails?.copy(
                  ratingCount = (result.data as MediaDetailsResult.RatingSuccess).rating,
                ),
              )

              is MediaDetailsResult.ReviewsSuccess -> {
                val data = result.data as MediaDetailsResult.ReviewsSuccess

                val updatedForms = viewState.forms.toMutableMap().apply {
                  this[data.formOrder] = DetailsForm.Content(DetailsData.Reviews(data.reviews))
                }

                viewState.copy(forms = updatedForms)
              }

              is MediaDetailsResult.SimilarSuccess -> {
                val data = result.data as MediaDetailsResult.SimilarSuccess

                val updatedForms = viewState.forms.toMutableMap().apply {
                  this[data.formOrder] = DetailsForm.Content(
                    DetailsData.Recommendations(data.similar),
                  )
                }
                viewState.copy(forms = updatedForms)
              }

              is MediaDetailsResult.VideosSuccess -> viewState.copy(
                trailer = (result.data as MediaDetailsResult.VideosSuccess).trailer,
              )

              is MediaDetailsResult.CreditsSuccess -> {
                val credits = (result.data as MediaDetailsResult.CreditsSuccess).aggregateCredits

                val castOrder = TvTab.Cast.order
                val updatedForms = viewState.forms.toMutableMap().apply {
                  this[castOrder] = DetailsForm.Content(
                    DetailsData.Cast(
                      isTv = true,
                      items = credits.cast,
                    ),
                  )
                }

                viewState.copy(
                  isLoading = false,
                  forms = updatedForms,
                )
              }

              is MediaDetailsResult.JellyseerrDetailsSuccess -> {
                val jellyseerrData = (result.data as? MediaDetailsResult.JellyseerrDetailsSuccess)
                  ?: return@onSuccess
                if (viewState.mediaType == MediaType.TV) {
                  val tvInfo = jellyseerrData.info
                  val updatedForms = getUpdatedSeasonForms(
                    tvInfo = tvInfo,
                    overrideSeasonStatus = true,
                  )

                  viewState.copy(
                    jellyseerrMediaInfo = tvInfo,
                    forms = updatedForms.first,
                    mediaDetails = (viewState.mediaDetails as? TV)?.copy(
                      seasons = updatedForms.second,
                    ),
                    actionButtons = findTvActions(tvInfo.status, updatedForms.second),
                  )
                } else {
                  viewState.copy(
                    jellyseerrMediaInfo = jellyseerrData.info,
                    actionButtons = findMovieActions(jellyseerrData.info.status),
                  )
                }
              }

              is MediaDetailsResult.AccountDetailsSuccess -> {
                val successData = (result.data as MediaDetailsResult.AccountDetailsSuccess)
                viewState.copy(
                  userDetails = successData.accountDetails,
                )
              }

              is MediaDetailsResult.MenuOptionsSuccess -> {
                val successData = (result.data as MediaDetailsResult.MenuOptionsSuccess)
                viewState.copy(
                  menuOptions = successData.menuOptions,
                )
              }

              is MediaDetailsResult.ActionButtonsSuccess -> {
                val successData = (result.data as MediaDetailsResult.ActionButtonsSuccess)
                viewState.copy(
                  actionButtons = successData.actionButtons,
                )
              }

              is MediaDetailsResult.Failure.FatalError -> viewState.copy(
                error = (result.data as MediaDetailsResult.Failure.FatalError).message,
                isLoading = false,
              )

              MediaDetailsResult.Failure.Unknown -> viewState.copy(
                error = MediaDetailsResult.Failure.Unknown.message,
                isLoading = false,
              )
            }
          }
        }.onFailure {
          if (it is MediaDetailsException) {
            _viewState.update { viewState ->
              viewState.copy(
                error = MediaDetailsResult.Failure.FatalError().message,
                isLoading = false,
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                error = MediaDetailsResult.Failure.Unknown.message,
                isLoading = false,
              )
            }
          }
        }
      }.launchIn(viewModelScope)

    viewModelScope.launch {
      spoilersObfuscationUseCase.invoke(Unit).collect { obfuscatedSpoilers ->
        _viewState.update { viewState ->
          viewState.copy(spoilersObfuscated = obfuscatedSpoilers.data)
        }
      }
    }
  }

  fun onSubmitRate(rating: Int) {
    setSectionState(AccountDataSection.Rating, true)
    viewModelScope.launch {
      submitRatingUseCase.invoke(
        SubmitRatingParameters(
          id = viewState.value.mediaId,
          mediaType = viewState.value.mediaType,
          rating = rating,
        ),
      ).fold(
        onSuccess = {
          _viewState.update { viewState ->
            viewState.copy(
              userDetails = viewState.userDetails.copy(rating = rating.toFloat()),
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.details__rating_submitted_successfully,
                  viewState.mediaDetails?.title ?: "",
                ),
              ),
            )
          }
          setSectionState(AccountDataSection.Rating, false)
        },
        onFailure = {
          if (it is SessionException.Unauthenticated) {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(R.string.details__must_be_logged_in_to_rate),
                  actionLabelText = UIText.ResourceText(R.string.login),
                  onSnackbarResult = ::navigateToLogin,
                ),
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(uiR.string.core_ui_error_retry),
                ),
              )
            }
          }
          setSectionState(AccountDataSection.Rating, false)
        },
      )
    }
  }

  fun onClearRating() {
    setSectionState(AccountDataSection.Rating, true)
    viewModelScope.launch {
      deleteRatingUseCase.invoke(
        DeleteRatingParameters(
          id = viewState.value.mediaId,
          mediaType = viewState.value.mediaType,
        ),
      ).fold(
        onSuccess = {
          _viewState.update { viewState ->
            viewState.copy(
              userDetails = viewState.userDetails.copy(rating = null),
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.details__rating_deleted_successfully,
                  viewState.mediaDetails?.title ?: "",
                ),
              ),
            )
          }
          setSectionState(AccountDataSection.Rating, false)
        },
        onFailure = {
          _viewState.update { viewState ->
            viewState.copy(
              snackbarMessage = SnackbarMessage.from(
                text = UIText.ResourceText(uiR.string.core_ui_error_retry),
              ),
            )
          }
          setSectionState(AccountDataSection.Rating, false)
        },
      )
    }
  }

  fun onAddToWatchlist() {
    setSectionState(AccountDataSection.Watchlist, true)
    viewModelScope.launch {
      addToWatchlistUseCase.invoke(
        AddToWatchlistParameters(
          id = viewState.value.mediaId,
          mediaType = viewState.value.mediaType,
          addToWatchlist = !viewState.value.userDetails.watchlist,
        ),
      ).fold(
        onSuccess = {
          _viewState.update { viewState ->
            if (viewState.userDetails.watchlist) {
              viewState.copy(
                userDetails = viewState.userDetails.copy(watchlist = false),
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.details__removed_from_watchlist,
                    viewState.mediaDetails?.title!!,
                  ),
                ),
              )
            } else {
              viewState.copy(
                userDetails = viewState.userDetails.copy(watchlist = true),
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.details__added_to_watchlist,
                    viewState.mediaDetails?.title!!,
                  ),
                ),
              )
            }
          }
          setSectionState(AccountDataSection.Watchlist, false)
        },
        onFailure = {
          if (it is SessionException.Unauthenticated) {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(R.string.details__must_be_logged_in_to_watchlist),
                  actionLabelText = UIText.ResourceText(R.string.login),
                  onSnackbarResult = ::navigateToLogin,
                ),
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(uiR.string.core_ui_error_retry),
                ),
              )
            }
          }
          setSectionState(AccountDataSection.Watchlist, false)
        },
      )
    }
  }

  fun onRequestMedia(seasons: List<Int>) {
    requestMediaUseCase(
      JellyseerrRequestParams(
        mediaId = viewState.value.mediaId,
        mediaType = viewState.value.mediaType.value,
        seasons = seasons,
      ),
    )
      .onStart {
        _viewState.update { viewState ->
          viewState.copy(isLoading = true)
        }
      }
      .onCompletion {
        _viewState.update { viewState ->
          viewState.copy(isLoading = false)
        }
      }
      .onEach { result ->
        result.onSuccess { response ->
          val message = response.message?.let { message ->
            UIText.StringText(message)
          } ?: UIText.ResourceText(
            R.string.feature_details_jellyseerr_success_media_request,
            viewState.value.mediaDetails?.title ?: "",
          )

          if (viewState.value.mediaType == MediaType.TV) {
            val tvInfo = response.mediaInfo
            val updatedForms = getUpdatedSeasonForms(
              tvInfo = tvInfo,
              overrideSeasonStatus = false,
            )

            val jellyseerrInfo = if (tvInfo.status == JellyseerrStatus.Media.UNKNOWN) {
              viewState.value.jellyseerrMediaInfo
            } else {
              tvInfo
            }

            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(message),
                jellyseerrMediaInfo = jellyseerrInfo,
                forms = updatedForms.first,
                mediaDetails = (viewState.mediaDetails as? TV)?.copy(
                  seasons = updatedForms.second,
                ),
                actionButtons = findTvActions(
                  tvStatus = jellyseerrInfo?.status ?: JellyseerrStatus.Media.UNKNOWN,
                  updatedForms.second,
                ),
              )
            }
          } else {
            _viewState.update { viewState ->
              viewState.copy(
                snackbarMessage = SnackbarMessage.from(message),
                jellyseerrMediaInfo = response.mediaInfo,
                actionButtons = findMovieActions(response.mediaInfo.status),
              )
            }
          }
        }.onFailure { error ->
          when (error) {
            is AppException.Forbidden -> setSnackbarMessage(
              SnackbarMessage.from(
                text = UIText.ResourceText(uiR.string.core_ui_jellyseerr_session_expired),
                actionLabelText = UIText.ResourceText(uiR.string.core_ui_login),
                duration = SnackbarDuration.Long,
                onSnackbarResult = ::navigateToLogin,
              ),
            )
            is AppException.Unauthorized -> setSnackbarMessage(
              SnackbarMessage.from(
                text = UIText.ResourceText(uiR.string.core_ui_jellyseerr_session_expired),
                actionLabelText = UIText.ResourceText(uiR.string.core_ui_login),
                duration = SnackbarDuration.Long,
                onSnackbarResult = ::navigateToJellyseerrAuth,
              ),
            )
            is AppException.Conflict -> setSnackbarMessage(
              SnackbarMessage.from(
                text = UIText.ResourceText(R.string.feature_details_jellyseerr_request_exists),
              ),
            )
            else -> setSnackbarMessage(
              SnackbarMessage.from(
                text = UIText.ResourceText(
                  R.string.feature_details_jellyseerr_request_failed,
                  viewState.value.mediaDetails?.title ?: "",
                ),
              ),
            )
          }
        }
      }.launchIn(viewModelScope)
  }

  fun onObfuscateSpoilers() {
    viewModelScope.launch {
      spoilersObfuscationUseCase.setSpoilersObfuscation(
        !viewState.value.spoilersObfuscated,
      )
    }
  }

  @VisibleForTesting
  fun navigateToLogin(snackbarResult: SnackbarResult) {
    if (snackbarResult == SnackbarResult.ActionPerformed) {
      _viewState.update { viewState ->
        viewState.copy(
          navigateToLogin = true,
          snackbarMessage = null,
        )
      }
    }
  }

  @VisibleForTesting
  fun navigateToJellyseerrAuth(snackbarResult: SnackbarResult) {
    if (snackbarResult == SnackbarResult.ActionPerformed) {
      viewModelScope.launch {
        _navigateToJellyseerrAuth.send(Unit)
        consumeSnackbarMessage()
      }
    }
  }

  fun onFetchAllRatings() {
    viewModelScope.launch {
      viewState.value.mediaDetails?.let {
        fetchAllRatingsUseCase(it).collect { result ->
          result
            .onSuccess { rating ->
              _viewState.update { viewState ->
                viewState.copy(
                  mediaDetails = viewState.mediaDetails?.copy(
                    ratingCount = viewState.mediaDetails.ratingCount.updateRating(
                      source = rating.first,
                      rating = rating.second,
                    ),
                  ),
                )
              }
            }.onFailure { error ->
              Timber.e(error)
            }
        }
      }
    }
  }

  fun onTabSelected(tab: Int) {
    _viewState.update { uiState ->
      uiState.copy(selectedTabIndex = tab)
    }
  }

  fun onMediaSourceClick(source: RatingSource) {
    val mediaDetails = viewState.value.mediaDetails ?: return
    val url = mediaDetails.externalUrl(source) ?: return

    viewModelScope.launch {
      if (url.isNotEmpty()) {
        _openUrlTab.send(url)
      }
    }
  }

  fun onDeleteRequest(id: Int) {
    _viewState.update {
      it.copy(isLoading = true)
    }
    viewModelScope.launch {
      deleteRequestUseCase
        .invoke(
          DeleteRequestParameters(
            requestId = id,
            mediaRequest = when (viewState.value.mediaType) {
              MediaType.TV -> MediaRequestApi.TV(viewState.value.mediaId)
              MediaType.MOVIE -> MediaRequestApi.Movie(viewState.value.mediaId)
              else -> MediaRequestApi.Unknown
            },
          ),
        )
        .collect { result ->
          result
            .onSuccess { mediaInfo ->
              _viewState.update { viewState ->
                if (viewState.mediaType == MediaType.TV) {
                  val updatedForms = getUpdatedSeasonForms(
                    tvInfo = mediaInfo,
                    overrideSeasonStatus = true,
                  )

                  viewState.copy(
                    jellyseerrMediaInfo = mediaInfo,
                    forms = updatedForms.first,
                    mediaDetails = (viewState.mediaDetails as? TV)?.copy(
                      seasons = updatedForms.second,
                    ),
                    actionButtons = findTvActions(
                      tvStatus = mediaInfo.status,
                      updatedForms.second,
                    ),
                    isLoading = false,
                    snackbarMessage = SnackbarMessage.from(
                      text = UIText.ResourceText(
                        R.string.feature_details_jellyseerr_success_request_delete,
                      ),
                    ),
                  )
                } else {
                  viewState.copy(
                    jellyseerrMediaInfo = mediaInfo,
                    actionButtons = findMovieActions(mediaInfo.status),
                    isLoading = false,
                    snackbarMessage = SnackbarMessage.from(
                      text = UIText.ResourceText(
                        R.string.feature_details_jellyseerr_success_request_delete,
                      ),
                    ),
                  )
                }
              }
            }
            .onFailure {
              _viewState.update { viewState ->
                viewState.copy(
                  isLoading = false,
                  snackbarMessage = SnackbarMessage.from(
                    text = UIText.ResourceText(
                      R.string.feature_details_jellyseerr_failed_request_delete,
                    ),
                  ),
                )
              }
            }
        }
    }
  }

  fun onDeleteMedia(deleteFile: Boolean) {
    _viewState.update {
      it.copy(isLoading = true)
    }
    viewModelScope.launch {
      viewState.value.jellyseerrMediaInfo?.mediaId?.let { mediaId ->
        deleteMediaUseCase.invoke(
          DeleteMediaParameters(
            mediaId = mediaId,
            deleteFile = deleteFile,
          ),
        )
          .onSuccess {
            _viewState.update { viewState ->
              viewState.copy(
                isLoading = false,
                jellyseerrMediaInfo = null,
                actionButtons = buildList {
                  addAll(DetailActionItem.defaultItems)
                  add(DetailActionItem.Request)
                },
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.feature_details_jellyseerr_success_media_delete,
                    viewState.mediaDetails?.title ?: "",
                  ),
                ),
                mediaDetails = viewState.mediaDetails.clearSeasonsStatus(),
                forms = if (viewState.mediaType == MediaType.TV) {
                  val form = _viewState.value.forms[TvTab.Seasons.order] as? DetailsForm.Content
                  val clearedSeasons =
                    (form?.data as? DetailsData.Seasons)?.items?.map { season ->
                      season.copy(status = null)
                    } ?: emptyList()

                  viewState.forms + mapOf(
                    TvTab.Seasons.order to DetailsForm.Content(
                      DetailsData.Seasons(clearedSeasons),
                    ),
                  )
                } else {
                  viewState.forms
                },
              )
            }
          }
          .onFailure {
            _viewState.update { viewState ->
              viewState.copy(
                isLoading = false,
                snackbarMessage = SnackbarMessage.from(
                  text = UIText.ResourceText(
                    R.string.feature_details_jellyseerr_failure_media_delete,
                    viewState.mediaDetails?.title ?: "",
                  ),
                ),
              )
            }
          }
      }
    }
  }

  private fun setSnackbarMessage(snackbarMessage: SnackbarMessage) {
    _viewState.update { viewState ->
      viewState.copy(
        snackbarMessage = snackbarMessage,
      )
    }
  }

  // Consumers
  fun consumeNavigateToLogin() {
    _viewState.update { viewState ->
      viewState.copy(navigateToLogin = null)
    }
  }

  fun consumeSnackbarMessage() {
    _viewState.update { viewState ->
      viewState.copy(snackbarMessage = null)
    }
  }

  private fun getAboutDetailsData(result: MediaDetailsResult.DetailsSuccess): DetailsData.About =
    DetailsData.About(
      overview = result.mediaDetails.overview,
      tagline = result.mediaDetails.tagline,
      genres = result.mediaDetails.genres,
      creators = when (result.mediaDetails) {
        is TV -> result.mediaDetails.creators
        is Movie -> result.mediaDetails.creators
        else -> null
      },
    )

  /**
   * @param overrideSeasonStatus If true, the season status will be set to UNKNOWN.
   * This is used only when requesting a TV show, to ensure that the seasons are updated
   * On delete request, we already get all the seasons with their correct status, since we
   * re-fetch all media details.
   */
  private fun getUpdatedSeasonForms(
    tvInfo: JellyseerrMediaInfo,
    overrideSeasonStatus: Boolean,
  ): Pair<Map<Int, DetailsForm<*>>, List<Season>> {
    val seasonsTabOrder = TvTab.Seasons.order

    val currentSeasonsForm = _viewState.value.forms[seasonsTabOrder] as? DetailsForm.Content
    val currentSeasonsData = (currentSeasonsForm?.data as? DetailsData.Seasons)?.items
      ?: emptyList()

    val updatedSeasons = currentSeasonsData.map { season ->
      val status = tvInfo.seasons.firstOrNull { it.seasonNumber == season.seasonNumber }?.status
        ?: if (overrideSeasonStatus) {
          null
        } else {
          season.status
        }

      season.copy(status = status)
    }

    val updatedForms = _viewState.value.forms + mapOf(
      seasonsTabOrder to DetailsForm.Content(
        DetailsData.Seasons(updatedSeasons),
      ),
    )
    return updatedForms to updatedSeasons.filterNot { it.seasonNumber == 0 }
  }

  private fun findTvActions(
    tvStatus: JellyseerrStatus,
    seasons: List<Season>,
  ): List<DetailActionItem> = buildList {
    addAll(DetailActionItem.defaultItems)
    if (seasons.any { it.isAvailable() } || tvStatus != JellyseerrStatus.Media.UNKNOWN) {
      add(DetailActionItem.ManageTvShow)
    }
    if (seasons.any { it.canBeRequested() }) {
      add(DetailActionItem.Request)
    }
  }

  private fun findMovieActions(status: JellyseerrStatus.Media): List<DetailActionItem> = buildList {
    addAll(DetailActionItem.defaultItems)
    if (status == JellyseerrStatus.Media.UNKNOWN) {
      add(DetailActionItem.Request)
    } else {
      add(DetailActionItem.ManageMovie)
    }
  }

  private fun setSectionState(
    section: AccountDataSection,
    loading: Boolean,
  ) {
    _viewState.update {
      it.copy(
        accountDataState = it.accountDataState + (section to loading),
      )
    }
  }
}
