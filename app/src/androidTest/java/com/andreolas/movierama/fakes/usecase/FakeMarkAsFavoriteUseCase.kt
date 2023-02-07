package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class FakeMarkAsFavoriteUseCase : MarkAsFavoriteUseCase(
    repository = FakeMoviesRepository(),
    dispatcher = MainDispatcherRule().testDispatcher,
) {
    private var resultForMarkAsFavoriteMap: MutableMap<PopularMovie, Unit> = mutableMapOf()

    fun mockResultMarkAsFavorite(
        popularMovie: PopularMovie,
        result: Unit,
    ) {
        resultForMarkAsFavoriteMap[popularMovie] = result
    }

    override suspend fun execute(parameters: PopularMovie) {
        return resultForMarkAsFavoriteMap[parameters]!!
    }
}
