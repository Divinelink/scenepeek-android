package com.divinelink.core.fixtures.details.media

import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.DetailsForm
import com.divinelink.core.model.details.media.DetailsForms
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab

object DetailsFormFactory {

  object Movie {
    fun loading() = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Loading
        MovieTab.Cast -> DetailsForm.Loading
        MovieTab.Recommendations -> DetailsForm.Loading
        MovieTab.Reviews -> DetailsForm.Loading
      }
    }

    fun empty() = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Content(DetailsDataFactory.Empty.about())
        MovieTab.Cast -> DetailsForm.Content(DetailsDataFactory.Empty.cast(isTv = false))
        MovieTab.Recommendations -> DetailsForm.Content(DetailsDataFactory.Empty.recommendations())
        MovieTab.Reviews -> DetailsForm.Content(DetailsDataFactory.Empty.reviews())
      }
    }

    fun full() = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Content(DetailsDataFactory.Movie.about())
        MovieTab.Cast -> DetailsForm.Content(DetailsDataFactory.Movie.cast())
        MovieTab.Recommendations -> DetailsForm.Content(DetailsDataFactory.Movie.recommendations())
        MovieTab.Reviews -> DetailsForm.Content(DetailsDataFactory.Movie.reviews())
      }
    }
  }

  object Tv {
    fun loading() = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Loading
        TvTab.Cast -> DetailsForm.Loading
        TvTab.Recommendations -> DetailsForm.Loading
        TvTab.Reviews -> DetailsForm.Loading
        TvTab.Seasons -> DetailsForm.Loading
      }
    }

    fun empty() = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Content(DetailsDataFactory.Empty.about())
        TvTab.Cast -> DetailsForm.Content(DetailsDataFactory.Empty.cast(isTv = true))
        TvTab.Recommendations -> DetailsForm.Content(DetailsDataFactory.Empty.recommendations())
        TvTab.Reviews -> DetailsForm.Content(DetailsDataFactory.Empty.reviews())
        TvTab.Seasons -> DetailsForm.Content(DetailsDataFactory.Empty.seasons())
      }
    }

    fun full() = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Content(DetailsDataFactory.Tv.about())
        TvTab.Cast -> DetailsForm.Content(DetailsDataFactory.Tv.cast())
        TvTab.Recommendations -> DetailsForm.Content(DetailsDataFactory.Tv.recommendations())
        TvTab.Reviews -> DetailsForm.Content(DetailsDataFactory.Tv.reviews())
        TvTab.Seasons -> DetailsForm.Content(DetailsDataFactory.Tv.seasons())
      }
    }
  }

  class MovieDetailsFormFactoryWizard(private var forms: Map<Int, DetailsForm<*>> = emptyMap()) {
    fun withAbout(about: DetailsData.About) = apply {
      forms = forms + (MovieTab.About.order to DetailsForm.Content(about))
    }

    fun withCast(cast: DetailsData.Cast) = apply {
      forms = forms + (MovieTab.Cast.order to DetailsForm.Content(cast))
    }

    fun withRecommendations(recommendations: DetailsData.Recommendations) = apply {
      forms = forms + (MovieTab.Recommendations.order to DetailsForm.Content(recommendations))
    }

    fun withReviews(reviews: DetailsData.Reviews) = apply {
      forms = forms + (MovieTab.Reviews.order to DetailsForm.Content(reviews))
    }

    fun build(): Map<Int, DetailsForm<*>> = forms
  }

  class TvDetailsFormFactoryWizard(private var forms: Map<Int, DetailsForm<*>> = emptyMap()) {
    fun withAbout(about: DetailsData.About) = apply {
      forms = forms + (TvTab.About.order to DetailsForm.Content(about))
    }

    fun withCast(cast: DetailsData.Cast) = apply {
      forms = forms + (TvTab.Cast.order to DetailsForm.Content(cast))
    }

    fun withRecommendations(recommendations: DetailsData.Recommendations) = apply {
      forms = forms + (TvTab.Recommendations.order to DetailsForm.Content(recommendations))
    }

    fun withReviews(reviews: DetailsData.Reviews) = apply {
      forms = forms + (TvTab.Reviews.order to DetailsForm.Content(reviews))
    }

    fun withSeasons(seasons: DetailsData.Seasons) = apply {
      forms = forms + (TvTab.Seasons.order to DetailsForm.Content(seasons))
    }

    fun build(): Map<Int, DetailsForm<*>> = forms
  }

  @JvmStatic
  fun DetailsForms.toMovieWzd(block: MovieDetailsFormFactoryWizard.() -> Unit) =
    MovieDetailsFormFactoryWizard(this).apply(block).build()

  fun DetailsForms.toTvWzd(block: TvDetailsFormFactoryWizard.() -> Unit) =
    TvDetailsFormFactoryWizard(this).apply(block).build()
}
