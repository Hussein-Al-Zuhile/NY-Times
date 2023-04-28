package com.hussein.nytimes.data.repositories

import com.hussein.nytimes.data.remote.response.BaseGetListResponse
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.models.Topic

interface TopicsRepository {

    suspend fun getMostViewedTopics(): State<List<Topic>>

}