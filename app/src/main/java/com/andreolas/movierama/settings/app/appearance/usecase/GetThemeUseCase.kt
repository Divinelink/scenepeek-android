package com.andreolas.movierama.settings.app.appearance.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.storage.PreferenceStorage
import com.andreolas.movierama.ui.theme.Theme
import com.andreolas.movierama.ui.theme.themeFromStorageKey
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Theme>(dispatcher) {
    override suspend fun execute(parameters: Unit): Theme {
        val selectedTheme = preferenceStorage.selectedTheme.first()
        return themeFromStorageKey(selectedTheme) ?: Theme.SYSTEM
//        when {
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Theme.SYSTEM
//                else -> Theme.SYSTEM
//            }
    }
}
