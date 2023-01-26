package com.andreolas.movierama.details.ui

import androidx.lifecycle.ViewModel
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
@Suppress("UnusedPrivateMember")
class DetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {

    private val _viewState: MutableStateFlow<DetailsViewState> = MutableStateFlow(
        value = DetailsViewState.Initial
    )
    val viewState: StateFlow<DetailsViewState> = _viewState.asStateFlow()
}
