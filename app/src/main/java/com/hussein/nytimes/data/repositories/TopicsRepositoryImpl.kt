package com.hussein.nytimes.data.repositories

import com.hussein.nytimes.data.base.BaseRepository
import com.hussein.nytimes.data.remote.RemoteDataSource
import com.hussein.nytimes.data.remote.RemoteService
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.models.Topic
import com.squareup.moshi.Moshi
import javax.inject.Inject


class TopicsRepositoryImpl @Inject constructor(
    moshi: Moshi,
    private val remoteService: RemoteService
) : BaseRepository(moshi),
    TopicsRepository {

    override suspend fun getMostViewedTopics(): State<List<Topic>> =
        sendRemoteRequestToGetList(remoteService::getMostViewedTopics)
}