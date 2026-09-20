package com.divinelink.core.fixtures.details.media

import com.divinelink.core.model.details.media.DetailSection
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.DetailsForm
import com.divinelink.core.model.details.media.DetailsForms
import com.divinelink.core.model.tab.MovieTab
import com.divinelink.core.model.tab.TvTab
import kotlin.jvm.JvmStatic

object DetailsFormFactory {

  object Movie {
    fun loading(id: Long) = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Loading
        MovieTab.Cast -> DetailsForm.Loading
        MovieTab.Recommendations -> DetailsForm.Section(DetailSection.MovieRecommendations(id))
        MovieTab.Reviews -> DetailsForm.Section(DetailSection.MovieReviews(id))
      }
    }

    fun error(id: Long) = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Error
        MovieTab.Cast -> DetailsForm.Error
        MovieTab.Recommendations -> DetailsForm.Section(DetailSection.MovieRecommendations(id))
        MovieTab.Reviews -> DetailsForm.Section(DetailSection.MovieReviews(id))
      }
    }

    fun empty(id: Long) = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Content(DetailsDataFactory.Empty.about())
        MovieTab.Cast -> DetailsForm.Content(DetailsDataFactory.Empty.cast(isTv = false))
        MovieTab.Recommendations -> DetailsForm.Section(DetailSection.MovieRecommendations(id))
        MovieTab.Reviews -> DetailsForm.Section(DetailSection.MovieReviews(id))
      }
    }

    fun full(id: Long) = MovieTab.entries.associate { tab ->
      tab.order to when (tab) {
        MovieTab.About -> DetailsForm.Content(DetailsDataFactory.Movie.about())
        MovieTab.Cast -> DetailsForm.Content(DetailsDataFactory.Movie.cast())
        MovieTab.Recommendations -> DetailsForm.Section(DetailSection.MovieRecommendations(id))
        MovieTab.Reviews -> DetailsForm.Section(DetailSection.MovieReviews(id))
      }
    }
  }

  object Tv {
    fun loading(id: Long) = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Loading
        TvTab.Cast -> DetailsForm.Loading
        TvTab.Recommendations -> DetailsForm.Section(DetailSection.TvRecommendations(id))
        TvTab.Reviews -> DetailsForm.Section(DetailSection.TvReviews(id))
        TvTab.Seasons -> DetailsForm.Loading
      }
    }

    fun error(id: Long) = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Error
        TvTab.Cast -> DetailsForm.Error
        TvTab.Recommendations -> DetailsForm.Section(DetailSection.TvRecommendations(id))
        TvTab.Reviews -> DetailsForm.Section(DetailSection.TvReviews(id))
        TvTab.Seasons -> DetailsForm.Error
      }
    }

    fun empty(id: Long) = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Content(DetailsDataFactory.Empty.about())
        TvTab.Cast -> DetailsForm.Content(DetailsDataFactory.Empty.cast(isTv = true))
        TvTab.Recommendations -> DetailsForm.Section(DetailSection.TvRecommendations(id))
        TvTab.Reviews -> DetailsForm.Section(DetailSection.TvReviews(id))
        TvTab.Seasons -> DetailsForm.Content(DetailsDataFactory.Empty.seasons())
      }
    }

    fun full(id: Long) = TvTab.entries.associate { tab ->
      tab.order to when (tab) {
        TvTab.About -> DetailsForm.Content(DetailsDataFactory.Tv.about())
        TvTab.Cast -> DetailsForm.Content(DetailsDataFactory.Tv.cast())
        TvTab.Recommendations -> DetailsForm.Section(DetailSection.TvRecommendations(id))
        TvTab.Reviews -> DetailsForm.Section(DetailSection.TvReviews(id))
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
