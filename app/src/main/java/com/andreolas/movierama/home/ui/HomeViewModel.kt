package com.andreolas.movierama.home.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> = _viewState
}
