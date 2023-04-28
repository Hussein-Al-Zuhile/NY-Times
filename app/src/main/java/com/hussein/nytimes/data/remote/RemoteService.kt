package com.hussein.nytimes.data.remote

import com.hussein.nytimes.data.base.RemoteGetListResponse
import com.hussein.nytimes.models.Topic
import retrofit2.http.GET

interface RemoteService {

    @GET("mostpopular/v2/mostviewed/all-sections/7.json")
    suspend fun getMostViewedTopics(): RemoteGetListResponse<Topic>

}