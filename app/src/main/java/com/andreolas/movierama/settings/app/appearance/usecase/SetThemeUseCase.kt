package com.andreolas.movierama.settings.app.appearance.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.base.di.PreferenceStorage
import com.andreolas.movierama.ui.theme.Theme
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class SetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Theme, Unit>(dispatcher) {
    override suspend fun execute(parameters: Theme) {
        preferenceStorage.selectTheme(parameters.storageKey)
    }
}
