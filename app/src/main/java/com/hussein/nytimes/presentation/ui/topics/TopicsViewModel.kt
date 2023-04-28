package com.hussein.nytimes.presentation.ui.topics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussein.nytimes.domain.base.State
import com.hussein.nytimes.domain.topics.GetMostViewedTopicsUseCase
import com.hussein.nytimes.domain.topics.GetTopicByIdUseCase
import com.hussein.nytimes.models.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val getMostViewedTopicsUseCase: GetMostViewedTopicsUseCase,
    private val getTopicByIdUseCase: GetTopicByIdUseCase,
) : ViewModel() {

    private var _topicsStateFlow =
        MutableStateFlow<State<List<Topic>>>(State.initial())
    val topicsStateFlow = _topicsStateFlow.asStateFlow()

    init {
        getMostViewTopics()
    }

    fun getMostViewTopics() {
        viewModelScope.launch {
            getMostViewedTopicsUseCase().collect {
                _topicsStateFlow.value = it
            }
        }
    }

    fun getTopicById(id: Long) = getTopicByIdUseCase(id)

}