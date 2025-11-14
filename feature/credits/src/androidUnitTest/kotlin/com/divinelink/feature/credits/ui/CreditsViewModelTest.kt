package com.divinelink.feature.credits.ui

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation.CreditsRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class CreditsViewModelTest {

  private var robot: CreditsViewModelTestRobot = CreditsViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test initialise viewModel`() = runTest {
    robot
      .withNavArgs(
        CreditsRoute(
          id = AggregatedCreditsFactory.credits().id,
          mediaType = MediaType.TV,
        ),
      )
      .buildViewModel()
      .assertUiState(CreditsUiState.initial())
  }

  @Test
  fun `test fetchCredits from useCase`() = runTest {
    robot
      .withNavArgs(
        CreditsRoute(
          id = AggregatedCreditsFactory.credits().id,
          mediaType = MediaType.TV,
        ),
      )
      .mockFetchCreditsUseCaseSuccess(flowOf(Result.success(AggregatedCreditsFactory.credits())))
      .buildViewModel()
      .assertUiState(
        CreditsUiState(
          forms = mapOf(
            CreditsTab.Cast(8) to CreditsUiContent.Cast(AggregatedCreditsFactory.credits().cast),
            CreditsTab.Crew(12) to
              CreditsUiContent.Crew(AggregatedCreditsFactory.credits().crewDepartments),
          ),
          tabs = listOf(
            CreditsTab.Cast(8),
            CreditsTab.Crew(12),
          ),
          selectedTabIndex = 0,
          obfuscateSpoilers = false,
        ),
      )
  }

  @Test
  fun `test flow emissions from useCase`() = runTest {
    val castChunked = AggregatedCreditsFactory.credits().cast.chunked(3)
    val crewChunked = AggregatedCreditsFactory.credits().crewDepartments

    // Set up a channel to emit multiple values
    val channel = Channel<Result<AggregateCredits>>()

    robot
      .withNavArgs(
        CreditsRoute(
          id = AggregatedCreditsFactory.credits().id,
          mediaType = MediaType.TV,
        ),
      )
      .setupChannelForUseCase(channel)
      .buildViewModel()
      .expectUiStates(
        action = {
          // Send the first emission
          launch {
            channel.send(
              Result.success(AggregatedCreditsFactory.credits().copy(cast = castChunked[0])),
            )
          }
          // Send the second emission
          launch {
            channel.send(
              Result.success(
                AggregatedCreditsFactory.credits().copy(cast = castChunked[0] + castChunked[1]),
              ),
            )
          }
          // Send the third emission
          launch {
            channel.send(
              Result.success(
                AggregatedCreditsFactory.credits()
                  .copy(cast = castChunked[0] + castChunked[1] + castChunked[2]),
              ),
            )
          }
        },
        uiStates = listOf(
          CreditsUiState.initial(),
          CreditsUiState(
            forms = mapOf(
              CreditsTab.Cast(3) to CreditsUiContent.Cast(castChunked[0]),
              CreditsTab.Crew(12) to CreditsUiContent.Crew(crewChunked),
            ),
            tabs = listOf(
              CreditsTab.Cast(3),
              CreditsTab.Crew(12),
            ),
            selectedTabIndex = 0,
            obfuscateSpoilers = false,
          ),
          CreditsUiState(
            forms = mapOf(
              CreditsTab.Cast(6) to CreditsUiContent.Cast(castChunked[0] + castChunked[1]),
              CreditsTab.Crew(12) to CreditsUiContent.Crew(crewChunked),
            ),
            tabs = listOf(
              CreditsTab.Cast(6),
              CreditsTab.Crew(12),
            ),
            selectedTabIndex = 0,
            obfuscateSpoilers = false,
          ),
          CreditsUiState(
            forms = mapOf(
              CreditsTab.Cast(8) to CreditsUiContent.Cast(
                castChunked[0] + castChunked[1] + castChunked[2],
              ),
              CreditsTab.Crew(12) to CreditsUiContent.Crew(crewChunked),
            ),
            tabs = listOf(
              CreditsTab.Cast(8),
              CreditsTab.Crew(12),
            ),
            selectedTabIndex = 0,
            obfuscateSpoilers = false,
          ),
        ),
      )
  }

  @Test
  fun `test onTabSelected`() = runTest {
    robot
      .withNavArgs(
        CreditsRoute(
          id = AggregatedCreditsFactory.credits().id,
          mediaType = MediaType.TV,
        ),
      )
      .mockFetchCreditsUseCaseSuccess(flowOf(Result.success(AggregatedCreditsFactory.credits())))
      .buildViewModel()
      .onTabSelected(1)
      .assertUiState(
        CreditsUiState(
          forms = mapOf(
            CreditsTab.Cast(8) to CreditsUiContent.Cast(AggregatedCreditsFactory.credits().cast),
            CreditsTab.Crew(12) to
              CreditsUiContent.Crew(AggregatedCreditsFactory.credits().crewDepartments),
          ),
          tabs = listOf(
            CreditsTab.Cast(8),
            CreditsTab.Crew(12),
          ),
          selectedTabIndex = 1,
          obfuscateSpoilers = false,
        ),
      )
  }

  @Test
  fun `test onObfuscateSpoilers with initial hidden`() = runTest {
    robot
      .withNavArgs(
        CreditsRoute(
          id = AggregatedCreditsFactory.credits().id,
          mediaType = MediaType.TV,
        ),
      )
      .mockFetchCreditsUseCaseSuccess(flowOf(Result.success(AggregatedCreditsFactory.credits())))
      .buildViewModel()
      .assertUiState(
        CreditsUiState(
          forms = mapOf(
            CreditsTab.Cast(8) to CreditsUiContent.Cast(AggregatedCreditsFactory.credits().cast),
            CreditsTab.Crew(12) to
              CreditsUiContent.Crew(AggregatedCreditsFactory.credits().crewDepartments),
          ),
          tabs = listOf(
            CreditsTab.Cast(8),
            CreditsTab.Crew(12),
          ),
          selectedTabIndex = 0,
          obfuscateSpoilers = false,
        ),
      )
      .onObfuscateSpoilers()
      .assertUiState(
        CreditsUiState(
          forms = mapOf(
            CreditsTab.Cast(8) to CreditsUiContent.Cast(AggregatedCreditsFactory.credits().cast),
            CreditsTab.Crew(12) to
              CreditsUiContent.Crew(AggregatedCreditsFactory.credits().crewDepartments),
          ),
          tabs = listOf(
            CreditsTab.Cast(8),
            CreditsTab.Crew(12),
          ),
          selectedTabIndex = 0,
          obfuscateSpoilers = true,
        ),
      )
      .onObfuscateSpoilers()
      .assertUiState(
        CreditsUiState(
          forms = mapOf(
            CreditsTab.Cast(8) to CreditsUiContent.Cast(AggregatedCreditsFactory.credits().cast),
            CreditsTab.Crew(12) to
              CreditsUiContent.Crew(AggregatedCreditsFactory.credits().crewDepartments),
          ),
          tabs = listOf(
            CreditsTab.Cast(8),
            CreditsTab.Crew(12),
          ),
          selectedTabIndex = 0,
          obfuscateSpoilers = false,
        ),
      )
  }
}
