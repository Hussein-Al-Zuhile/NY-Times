package com.hussein.nytimes.data.base

import android.util.Log
import com.hussein.nytimes.data.remote.response.BaseGetListResponse
import com.hussein.nytimes.data.remote.response.ErrorBody
import com.hussein.nytimes.domain.base.State
import com.squareup.moshi.Moshi
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.concurrent.CancellationException

typealias RemoteGetListResponse<T> = Response<BaseGetListResponse<T>>

abstract class BaseRepository(private val moshi: Moshi) {

    protected suspend fun <T> sendRemoteRequestToGetList(request: suspend () -> RemoteGetListResponse<T>): State<List<T>> {
        var state: State<List<T>>

        try {
            val response = request()

            state = when {
                response.isSuccessful -> {
                    val baseBody = response.body()
                    State.success(baseBody?.results)
                }

                response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> {

                    State.Failure.Unauthorized(null)
                }

                else -> {
                    val errorBody = response.errorBody()?.let {
                        moshi.adapter(ErrorBody::class.java).fromJson(it.string())
                    }
                    State.failure(errorBody?.fault?.message)
                }
            }

        } catch (throwable: Throwable) {
            Log.e("Remote request error", "", throwable)

            if (throwable is CancellationException) throw throwable

            state = State.failure()
        }

        return state
    }
}