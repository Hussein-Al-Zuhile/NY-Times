package com.hussein.nytimes.domain.topics

import com.hussein.nytimes.data.repositories.TopicsRepository
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.domain.base.UseCase
import com.hussein.nytimes.models.Topic
import com.hussein.nytimes.utility.ConnectionManager
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMostViewedTopicsUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val connectionManager: ConnectionManager,
) : UseCase() {

    operator fun invoke() = flow<State<List<Topic>>> {
        if (connectionManager.isConnected.not()) {
            emit(State.Failure.InternetUnavailable())
            return@flow
        }

        emit(State.loading())

        emit(topicsRepository.getMostViewedTopics())
    }
}