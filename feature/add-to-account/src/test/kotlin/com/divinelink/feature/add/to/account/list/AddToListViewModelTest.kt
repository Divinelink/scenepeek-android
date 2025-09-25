package com.divinelink.feature.add.to.account.list

import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.list.ListException
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation.AddToListRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.add.to.account.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class AddToListViewModelTest {

  object AddToListRouteFactory {
    fun movie() = AddToListRoute(
      id = 1234,
      mediaType = MediaType.MOVIE,
    )
  }

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val robot = AddToListViewModelTestRobot()

  @Test
  fun `test loadMore lists when page is less than total pages`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserLists(response = Result.success(ListItemFactory.page2()))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 3,
            lists = ListData.Data(
              PaginationData(
                page = 2,
                totalPages = 2,
                totalResults = 6,
                list = ListItemFactory.page1().list + ListItemFactory.page2().list,
              ),
            ),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test observe changes on fetch user lists`() = runTest {
    val channel: Channel<Result<PaginationData<ListItem>>> = Channel()

    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(channel)
      .buildViewModel()
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()),
      )
      .expectUiStates(
        action = {
          launch {
            channel.send(Result.success(ListItemFactory.page1()))
          }

          launch {
            channel.send(
              Result.success(
                ListItemFactory.page1().copy(
                  list = listOf(
                    ListItemFactory.movies().copy(
                      numberOfItems = 6,
                    ),
                    ListItemFactory.shows(),
                    ListItemFactory.recommended(),
                  ),
                ),
              ),
            )
          }

          launch {
            channel.send(
              Result.success(
                ListItemFactory.page1().copy(
                  list = listOf(
                    ListItemFactory.movies().copy(
                      numberOfItems = 6,
                    ),
                    ListItemFactory.shows(),
                    ListItemFactory.recommended().copy(
                      numberOfItems = 16,
                    ),
                  ),
                ),
              ),
            )
          }
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(
              PaginationData(
                page = 1,
                totalPages = 2,
                totalResults = 6,
                list = listOf(
                  ListItemFactory.movies().copy(
                    numberOfItems = 6,
                  ),
                  ListItemFactory.shows(),
                  ListItemFactory.recommended(),
                ),
              ),
            ),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(
              PaginationData(
                page = 1,
                totalPages = 2,
                totalResults = 6,
                list = listOf(
                  ListItemFactory.movies().copy(
                    numberOfItems = 6,
                  ),
                  ListItemFactory.shows(),
                  ListItemFactory.recommended().copy(
                    numberOfItems = 16,
                  ),
                ),
              ),
            ),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test loadMore lists with generic error does not remove pages`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserLists(response = Result.failure(Exception("Foo")))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test loadMore lists with unauthenticated error clears lists`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserLists(response = Result.failure(SessionException.Unauthenticated()))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            isLoading = false,
            error = BlankSlateState.Unauthenticated(
              description = UIText.ResourceText(
                R.string.feature_add_to_account_list_login_description,
              ),
              retryText = UIText.ResourceText(com.divinelink.core.ui.R.string.core_ui_login),
            ),
          ),
        ),
      )
  }

  @Test
  fun `test onAddToList with success`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .mockAddItemToList(Result.success(true))
      .expectUiStates(
        action = {
          // Add to first item in the list
          onListClick(ListItemFactory.movies().id)
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = true,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(
              PaginationData(
                page = 1,
                totalPages = 2,
                totalResults = 6,
                list = listOf(
                  ListItemFactory.movies(),
                  ListItemFactory.shows(),
                  ListItemFactory.recommended(),
                ),
              ),
            ),
            isLoading = false,
            loadingMore = false,
            addedToLists = setOf(8452376),
            displayMessage = DisplayMessage.Success(
              UIText.ResourceText(
                R.string.feature_add_to_account_item_added_to_list_success,
                ListItemFactory.page1().list.first().name,
              ),
            ),
          ),
        ),
      )
  }

  @Test
  fun `test onAddToList when item already exists`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .mockAddItemToList(
        Result.failure(ListException.ItemAlreadyExists()),
      )
      .expectUiStates(
        action = {
          // Add to first item in the list
          onListClick(ListItemFactory.page1().list.first().id)
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = true,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
            displayMessage = DisplayMessage.Error(
              UIText.ResourceText(
                R.string.feature_add_to_account_item_added_to_list_failure,
                ListItemFactory.page1().list.first().name,
              ),
            ),
          ),
        ),
      )
  }

  @Test
  fun `test onAddToList with unexpected error`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .mockAddItemToList(
        Result.failure(ListException.UnexpectedError()),
      )
      .expectUiStates(
        action = {
          // Add to first item in the list
          onListClick(ListItemFactory.page1().list.first().id)
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = true,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
            displayMessage = DisplayMessage.Error(
              UIText.ResourceText(
                R.string.feature_add_to_account_item_add_to_list_unexpected_failure,
                ListItemFactory.page1().list.first().name,
              ),
            ),
          ),
        ),
      )
  }

  @Test
  fun `test ConsumeDisplayMessage clears displayMessage`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .mockAddItemToList(
        Result.failure(ListException.UnexpectedError()),
      )
      .onListClick(ListItemFactory.page1().list.first().id)
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
          displayMessage = DisplayMessage.Error(
            UIText.ResourceText(
              R.string.feature_add_to_account_item_add_to_list_unexpected_failure,
              ListItemFactory.page1().list.first().name,
            ),
          ),
        ),
      )
      .onConsumeDisplayMessage()
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
          displayMessage = null,
        ),
      )
  }

  @Test
  fun `test onAddToList with unauthenticated error`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .mockAddItemToList(
        Result.failure(SessionException.Unauthenticated()),
      )
      .expectUiStates(
        action = {
          // Add to first item in the list
          onListClick(ListItemFactory.page1().list.first().id)
        },
        uiStates = listOf(
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = true,
            loadingMore = false,
          ),
          AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
            page = 1,
            lists = ListData.Initial,
            isLoading = false,
            loadingMore = false,
            error = BlankSlateState.Unauthenticated(
              description = UIText.ResourceText(
                R.string.feature_add_to_account_list_login_description,
              ),
              retryText = UIText.ResourceText(com.divinelink.core.ui.R.string.core_ui_login),
            ),
          ),
        ),
      )
  }

  @Test
  fun `test onLogin emits navigateToTMDBAuth`() = runTest {
    robot
      .withArgs(AddToListRouteFactory.movie())
      .mockFetchUserLists(
        Result.failure(SessionException.Unauthenticated()),
      )
      .buildViewModel()
      .assertUiState(
        AddToListUiState.initial(AddToListRouteFactory.movie()).copy(
          page = 1,
          lists = ListData.Initial,
          isLoading = false,
          loadingMore = false,
          error = BlankSlateState.Unauthenticated(
            description = UIText.ResourceText(
              R.string.feature_add_to_account_list_login_description,
            ),
            retryText = UIText.ResourceText(com.divinelink.core.ui.R.string.core_ui_login),
          ),
        ),
      )
      .expectNoNavigateToTMDBAuth()
      .onLogin()
      .awaitNavigateToTMDBAuth()
  }
}
