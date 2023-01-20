package com.andreolas.movierama.home

import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.home.ui.HomeViewState
import com.andreolas.movierama.settings.app.AppSettingsActivity
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.getString
import com.andreolas.movierama.ui.theme.topBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    viewState: HomeViewState,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollColors = TopAppBarDefaults.smallTopAppBarColors(
        scrolledContainerColor = topBarColor(),
    )

    val context = LocalContext.current
    Scaffold(
        contentWindowInsets = WindowInsets(
            left = 0.dp,
            top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding().value.dp,
            right = 0.dp,
            bottom = 0.dp,
        ),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            // todo
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = UIText.StringText("Home").getString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            context.startActivity(
                                Intent(context, AppSettingsActivity::class.java)
                            )
                        },
                    ) {
                        Icon(Icons.Filled.Settings, null)
                    }
                },
                navigationIcon = {
                    // todo
                },
                scrollBehavior = scrollBehavior,
                colors = scrollColors,
            )
        },
    ) { _ ->

        //        if (viewState is BeanTrackerViewState.Completed) {
        //            BeansList(
        //                viewState.beans,
        //                modifier = modifier
        //                    .padding(top = paddingValues.calculateTopPadding()),
        //                onBeanClicked = onBeanClicked,
        //                state = scrollState,
        //                bottomPadding = bottomPadding,
        //            )
        //        }
    }
    //
    //    if (viewState.showLoading) {
    //        BeansLoadingContent()
    //    }
}
