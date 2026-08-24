package com.divinelink.feature.home.section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.home.HomeForm
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.model.home.toRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class SectionViewModel(
  section: MediaListSection.Home,
  private val repository: MediaRepository,
  private val clock: Clock,
) : ViewModel() {

  private val _uiState: MutableStateFlow<HomeSectionUiState> = MutableStateFlow(
    HomeSectionUiState.initial(section),
  )
  val uiState: StateFlow<HomeSectionUiState> = _uiState

  init {
    fetchSections()
  }

  private fun fetchSections() {
    when (uiState.value.form) {
      is HomeForm.Data -> Unit
      HomeForm.Error -> fetchMediaSection()
      HomeForm.Initial -> fetchMediaSection()
      is HomeForm.SeerrData -> Unit
    }
  }

  private fun fetchMediaSection() {
    when (uiState.value.section) {
      is MediaListSection.Popular,
      is MediaListSection.Upcoming,
        -> fetchMediaLists()
      MediaListSection.TrendingAll -> fetchTrending()
      is MediaListSection.RecentRequests -> _uiState.update { state ->
        state.copy(form = HomeForm.SeerrData(filter = MediaRequestFilter.All))
      }
    }
  }

  private fun handleResult(
    result: Result<PaginationData<MediaItem>>,
  ) {
    result.onSuccess { response ->
      _uiState.update { uiState ->
        val data = (uiState.form as? HomeForm.Data)?.pages ?: mapOf(
          1 to response.list,
        )

        uiState.copy(
          form = HomeForm.Data(
            pages = data.plus(response.page to response.list),
            canLoadMore = response.canLoadMore(),
            hasError = false,
            isLoading = false,
          ),
          page = response.page,
        )
      }
    }.onFailure {
      if (getPage() == 1) {
        setWholeCurrentFormToError()
      } else {
        setErrorOnCurrentSectionWithData()
      }
    }
  }

  private fun setWholeCurrentFormToError() {
    _uiState.update { uiState ->
      uiState.copy(
        form = HomeForm.Error,
      )
    }
  }

  private fun setErrorOnCurrentSectionWithData() {
    _uiState.update { uiState ->
      val currentForm = uiState.form
      val pages = (currentForm as? HomeForm.Data)?.pages ?: emptyMap()

      uiState.copy(
        form = HomeForm.Data(
          pages = pages,
          canLoadMore = false,
          isLoading = false,
          hasError = true,
        ),
      )
    }
  }

  private fun onLoadNextPage() {
    val currentForm = uiState.value.form
    val form = currentForm as? HomeForm.Data

    if (form?.hasError == false && form.canLoadMore) {
      fetchMediaSection()
    }
  }

  private fun onRetrySection() {
    val newForm = when (val currentForm = uiState.value.form) {
      is HomeForm.Data -> currentForm.copy(
        hasError = false,
        canLoadMore = false,
        isLoading = true,
      )
      is HomeForm.SeerrData -> currentForm
      HomeForm.Error -> HomeForm.Initial
      HomeForm.Initial -> HomeForm.Initial
    }

    _uiState.update { uiState ->
      uiState.copy(
        form = newForm,
      )
    }

    fetchMediaSection()
  }

  fun onAction(action: HomeSectionAction) {
    when (action) {
      is HomeSectionAction.LoadMore -> onLoadNextPage()
      is HomeSectionAction.RetrySection -> onRetrySection()
    }
  }

  private fun getPage(): Int = uiState.value.page + 1

  private fun fetchMediaLists() {
    val today = clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

    val request = uiState.value.section.toRequest(
      mediaType = MediaType.UNKNOWN,
      minDate = today,
    ) ?: return

    viewModelScope.launch {
      repository.fetchMediaLists(
        request = request,
        page = getPage(),
      )
        .catch { emit(Result.failure(it)) }
        .collect { result ->
          handleResult(
            result = result,
          )
        }
    }
  }

  private fun fetchTrending() {
    viewModelScope.launch {
      repository.fetchTrending(
        page = getPage(),
      )
        .catch { emit(Result.failure(it)) }
        .collect { result ->
          handleResult(
            result = result,
          )
        }
    }
  }
}
