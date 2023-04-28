package com.hussein.nytimes.presentation.ui.topics

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.models.Media
import com.hussein.nytimes.models.MediaMetadata
import com.hussein.nytimes.models.Topic
import com.hussein.nytimes.presentation.ui.theme.NYTimesTheme
import com.hussein.nytimes.utility.LightAndNightPreviews
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.log

@Composable
fun MostViewedTopicsScreen(
    topicsStateFlow: Flow<State<List<Topic>>>,
    onItemClicked: (Topic) -> Unit,
    onRetry: () -> Unit
) {

    val topicsState =
        topicsStateFlow.collectAsStateWithLifecycle(initialValue = State.initial()).value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = topicsState, block = {
        if (topicsState.isFailure) {
            topicsState.message?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    })
    Surface() {
        Box(modifier = Modifier.fillMaxSize()) {
            when (topicsState) {
                is State.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is State.Success.Data -> {
                    LazyColumn(
                        content = {
                            items(items = topicsState.data, key = { it.id }) {
                                TopicItem(topic = it, onItemClicked = onItemClicked)
                            }
                        },
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    )
                }

                is State.Failure -> {
                    Button(onClick = onRetry, modifier = Modifier.align(Alignment.Center)) {
                        Text(text = "Retry")
                    }
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

@Composable
fun TopicItem(modifier: Modifier = Modifier, topic: Topic, onItemClicked: (Topic) -> Unit) {
    ElevatedCard(
        modifier
            .clip(CardDefaults.elevatedShape)
            .clickable() { onItemClicked(topic) }
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            val (firstMediaCaption, firstMediaUrls) = topic.imagesUrlsWithItsCaption.first()
            AsyncImage(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, end = 15.dp)
                    .width(50.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .align(Alignment.Top)
                    .background(Color.Gray),
                model = firstMediaUrls.first(),
                contentDescription = firstMediaCaption,
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = topic.title, style = MaterialTheme.typography.titleMedium,
                    maxLines = 3
                )
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = topic.details,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Publishing date"
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = topic.publishedDate,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@LightAndNightPreviews
@Composable
fun TopicItemPreview() {
    NYTimesTheme() {
        TopicItem(topic = previewTopics.first(), onItemClicked = {})
    }
}


@LightAndNightPreviews
@Composable
fun MostViewedTopicsScreenPreview() {
    NYTimesTheme() {
        MostViewedTopicsScreen(
            flow {
                emit(State.loading())
                delay(2000)
                emit(
                    State.failure("SSSSS")
                )
            }, onItemClicked = {}, {})
    }
}

val previewTopics = listOf(
    Topic(
        1,
        "",
        "",
        "2023-04-27",
        "",
        "Airman Accused of Leak Has History of Racist and Violent Remarks, Filing Says",
        "Prosecutors accused Jack Teixeira of trying to cover up his actions and described a possible propensity toward violence.",
        listOf(
            Media(
                "image",
                "Prosecutors’ filing cast Airman Teixeira’s actions, which had previously been seen as mainly motivated by his desire to show off to younger friends online, in a somewhat darker light.",
                listOf(
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-thumbStandard.jpg"),
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-mediumThreeByTwo210.jpg"),
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-mediumThreeByTwo440.jpg"),
                )
            )
        )
    ),
    Topic(
        2, "", "", "2023-04-27", "", "Title 2", "",
        listOf(
            Media(
                "image",
                "Prosecutors’ filing cast Airman Teixeira’s actions, which had previously been seen as mainly motivated by his desire to show off to younger friends online, in a somewhat darker light.",
                listOf(
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-thumbStandard.jpg"),
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-mediumThreeByTwo210.jpg"),
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-mediumThreeByTwo440.jpg"),
                )
            )
        )
    ),
    Topic(
        3, "", "", "2023-04-27", "", "Title 5", "",
        listOf(
            Media(
                "image",
                "Prosecutors’ filing cast Airman Teixeira’s actions, which had previously been seen as mainly motivated by his desire to show off to younger friends online, in a somewhat darker light.",
                listOf(
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-thumbStandard.jpg"),
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-mediumThreeByTwo210.jpg"),
                    MediaMetadata("https://static01.nyt.com/images/2023/05/22/multimedia/22dc-leaks-01-hfbj/22dc-leaks-01-hfbj-mediumThreeByTwo440.jpg"),
                )
            )
        )
    ),
)
