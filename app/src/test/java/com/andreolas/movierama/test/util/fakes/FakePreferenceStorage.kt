package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.base.di.PreferenceStorage
import kotlinx.coroutines.flow.MutableStateFlow

open class FakePreferenceStorage(
    selectedTheme: String = "",
) : PreferenceStorage {

    private val _selectedTheme = MutableStateFlow(selectedTheme)
    override val selectedTheme = _selectedTheme

    override suspend fun selectTheme(theme: String) {
        _selectedTheme.value = theme
    }
}
