package com.hussein.nytimes.presentation.ui.topics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hussein.nytimes.R
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.domain.base.fallbackMessage
import com.hussein.nytimes.models.Topic
import com.hussein.nytimes.presentation.ui.theme.NYTimesTheme
import com.hussein.nytimes.utility.LightAndNightPreviews

@Composable
fun TopicDetailsScreen(topicState: State<Topic>, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(key1 = topicState, block = {
        if (topicState.isFailure) {
            val message = topicState.message
                ?: topicState.fallbackMessage?.let { context.getString(it) }
            message?.let { snackbarHostState.showSnackbar(it) }
        }
    })

    val scrollState = rememberScrollState()
    Surface {
        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            when (topicState) {
                is State.Success.Data -> {
                    TopicDetails(topic = topicState.data)
                }

                is State.Failure -> {
                    SnackbarHost(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        hostState = snackbarHostState
                    )
                }

                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopicDetails(modifier: Modifier = Modifier, topic: Topic) {
    Surface(modifier) {
        Column() {
            val captionWithHighestResolutionImages =
                topic.imagesUrlsWithItsCaption.map { (caption, imageResolutionUrls) ->
                    // The last image in each media item is the highest resolution, so we use it
                    caption to imageResolutionUrls.last()
                }
            HorizontalPager(
                pageCount = captionWithHighestResolutionImages.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(Color.Gray)
            ) {
                val (caption, image) = captionWithHighestResolutionImages[it]
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = image,
                    contentDescription = caption,
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = topic.title,
                style = MaterialTheme.typography.headlineMedium,
            )
            Row(
                Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.content_description_source))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = topic.source,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.content_description_publishing_date)
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = topic.publishedDate,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Text(
                text = topic.details,
                Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@LightAndNightPreviews
@Composable
fun TopicDetailsPreview() {
    NYTimesTheme() {
        TopicDetails(topic = previewTopics.first())
    }
}

@LightAndNightPreviews
@Composable
fun TopicDetailsScreenPreview() {
    NYTimesTheme() {
        TopicDetailsScreen(State.success(previewTopics.first()))
    }
}