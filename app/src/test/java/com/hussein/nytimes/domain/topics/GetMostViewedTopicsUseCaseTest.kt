package com.hussein.nytimes.domain.topics

import com.hussein.nytimes.data.remote.RemoteService
import com.hussein.nytimes.data.remote.response.BaseGetListResponse
import com.hussein.nytimes.data.repositories.TopicsRepository
import com.hussein.nytimes.data.repositories.TopicsRepositoryImpl
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.presentation.ui.topics.previewTopics
import com.hussein.nytimes.utility.ConnectionManager
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

/**
 * This is an integration test class that's test
 * both topics repository and getMostViewedTopicsUseCase.
 *
 * That's will ensure that they are working together properly
 * and return the required state regardless of RemoteService implementation
 * And this isolation what's makes the test very easy and effective.
 */

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetMostViewedTopicsUseCaseTest {

    lateinit var topicsRepository: TopicsRepository
    lateinit var getMostViewedTopicsUseCase: GetMostViewedTopicsUseCase

    @Mock
    lateinit var remoteService: RemoteService

    @Mock
    lateinit var connectionManager: ConnectionManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        topicsRepository = TopicsRepositoryImpl(Moshi.Builder().build(), remoteService)
        getMostViewedTopicsUseCase = GetMostViewedTopicsUseCase(topicsRepository, connectionManager)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `Return null body from network then the state is failure`() {
        runTest {
            Mockito.`when`(remoteService.getMostViewedTopics()).thenReturn(
                Response.success(
                    null
                )
            )
            getMostViewedTopicsUseCase().collect {
                if (it.isLoading.not()) {
                    assert(it.isFailure)
                }
            }
        }
    }

    @Test
    fun `Return corrupted json from network then the state is failure`() {
        runTest {
            Mockito.`when`(remoteService.getMostViewedTopics()).thenReturn(
                Response.success(
                    null,
                    okhttp3.Response.Builder()
                        .code(200)
                        .body("""{"data": {}}""".toResponseBody())
                        // these parameters are required form okhttp,
                        // that's why we filled it with fake data
                        .message("")
                        .protocol(Protocol.H2_PRIOR_KNOWLEDGE)
                        .request(Request("http://mockedUrl".toHttpUrl()))
                        .build()
                )
            )
            getMostViewedTopicsUseCase().collect {
                if (it.isLoading.not()) {
                    assert(it.isFailure)
                }
            }
        }
    }

    @Test
    fun `If there is no internet then the state is State_Failure_InternetUnavailable`() {
        runTest {
            Mockito.`when`(connectionManager.isConnected).thenReturn(
                false
            )
            getMostViewedTopicsUseCase().collect {
                if (it.isLoading.not()) {
                    assert(it is State.Failure.InternetUnavailable)
                }
            }
        }
    }
}