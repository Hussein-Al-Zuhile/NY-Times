package com.hussein.nytimes.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.presentation.ui.theme.NYTimesTheme
import com.hussein.nytimes.presentation.ui.topics.MostViewedTopicsScreen
import com.hussein.nytimes.presentation.ui.topics.TopicsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYTimesTheme {
                val topicsViewModel = hiltViewModel<TopicsViewModel>()
                MostViewedTopicsScreen(
                    topicsStateFlow = topicsViewModel.topicsStateFlow,
                    onItemClicked = {},
                    onRetry = topicsViewModel::getMostViewTopics
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NYTimesTheme {
        MostViewedTopicsScreen(topicsStateFlow = flow { }, onItemClicked = {}, {})
    }
}