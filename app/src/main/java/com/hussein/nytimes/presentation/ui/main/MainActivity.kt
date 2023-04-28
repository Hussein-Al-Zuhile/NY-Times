package com.hussein.nytimes.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.presentation.ui.theme.NYTimesTheme
import com.hussein.nytimes.presentation.ui.topics.MostViewedTopicsScreen
import com.hussein.nytimes.presentation.ui.topics.TopicDetailsScreen
import com.hussein.nytimes.presentation.ui.topics.TopicsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYTimesTheme {
                val navController = rememberNavController()
                TopicsNavigation(navController = navController)
            }
        }
    }
}

@Composable
fun TopicsNavigation(
    modifier: Modifier = Modifier, navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopicRoute.TopicsListScreen.route,
    ) {
        composable(TopicRoute.TopicsListScreen.route) {
            val topicsViewModel = hiltViewModel<TopicsViewModel>()
            MostViewedTopicsScreen(
                topicsStateFlow = topicsViewModel.topicsStateFlow,
                onItemClicked = {
                    navController.navigate(TopicRoute.TopicDetailsScreen.createRoute(it.id.toString()))
                },
                onRetry = topicsViewModel::getMostViewTopics
            )
        }
        composable(
            TopicRoute.TopicDetailsScreen.route, arguments =
            listOf(navArgument("topicId") {
                type = NavType.StringType
            })
        ) {
            val topicId = it.arguments?.getString("topicId")
            val topicsViewModel = hiltViewModel<TopicsViewModel>()
            val topicState =
                topicId?.toLongOrNull()?.let { it1 -> topicsViewModel.getTopicById(it1) }
                    ?: State.Failure.ItemNotFound()
            TopicDetailsScreen(topicState)
        }
    }
}

sealed class TopicRoute(val route: String) {
    object TopicsListScreen : TopicRoute("topics")
    object TopicDetailsScreen : TopicRoute("topics/{topicId}") {
        fun createRoute(topicId: String) = "topics/$topicId"
    }
}