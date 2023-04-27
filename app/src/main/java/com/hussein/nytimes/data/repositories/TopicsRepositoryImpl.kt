package com.hussein.nytimes.data.repositories

import com.hussein.nytimes.data.base.BaseRepository
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.models.Topic
import com.squareup.moshi.Moshi
import javax.inject.Inject


class TopicsRepositoryImpl @Inject constructor(moshi: Moshi) : BaseRepository(moshi),
    TopicsRepository {

    override fun getMostViewedTopics(): State<List<Topic>> {
        TODO("Not yet implemented")
    }
}