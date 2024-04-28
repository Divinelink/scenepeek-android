package com.andreolas.movierama

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.andreolas.movierama.destinations.HomeScreenDestination
import com.andreolas.movierama.home.ui.HomeScreen
import com.andreolas.movierama.home.ui.LoadingContent
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.updateForTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.setNavigationBarColor
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
    setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground))

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.theme.collect { theme ->
            updateForTheme(theme)
          }
        }
      }
    }

    setContent {
      AppTheme {
        Surface(
          color = MaterialTheme.colorScheme.background,
        ) {
          val mainState = viewModel.viewState.collectAsState()

          when (mainState.value) {
            MainViewState.Completed -> {
              AppNavHost(
                startRoute = HomeScreenDestination,
              )
            }
            MainViewState.Loading -> {
              LoadingContent()
            }
            is MainViewState.Error -> {
              viewModel.retryFetchRemoteConfig()
            }
          }
        }
      }
    }
  }

  @OptIn(ExperimentalMaterialNavigationApi::class)
  @Composable
  private fun AppNavHost(
    startRoute: Route,
  ) {
    DestinationsNavHost(
      startRoute = startRoute,
      navGraph = NavGraphs.root,
      engine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
          enterTransition = {
            slideInHorizontally()
          },
          exitTransition = {
            fadeOut()
          },
        ),
      ),
      manualComposableCallsBuilder = {
        composable(HomeScreenDestination) {
          HomeScreen(
            navigator = destinationsNavigator,
          )
        }
      }
    )
  }
}
