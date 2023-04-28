package com.hussein.nytimes.data.repositories

import android.util.Log
import com.hussein.nytimes.data.base.BaseRepository
import com.hussein.nytimes.data.remote.RemoteDataSource
import com.hussein.nytimes.data.remote.RemoteService
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.models.Topic
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicsRepositoryImpl @Inject constructor(
    moshi: Moshi,
    private val remoteService: RemoteService
) : BaseRepository(moshi),
    TopicsRepository {

    override var topics: List<Topic> = emptyList()
    override suspend fun getMostViewedTopics(): State<List<Topic>> =
        sendRemoteRequestToGetList(remoteService::getMostViewedTopics).apply {
            dataOrNull?.let {
                topics = it
                Log.e("SSAAAAA", "getMostViewedTopics: $topics")
            }
        }

    override fun getTopicById(id: Long): State<Topic> {
        Log.e(
            "SSSSS",
            "getTopicById: $id" +
                    " $topics",
        )
        return topics.find { it.id == id }?.let {
            State.success(it)
        } ?: State.Failure.ItemNotFound()
    }
}