package com.hussein.nytimes.domain.topics

import com.hussein.nytimes.data.repositories.TopicsRepository
import com.hussein.nytimes.domain.base.UseCase
import javax.inject.Inject

class GetTopicByIdUseCase @Inject constructor(private val topicsRepository: TopicsRepository) :
    UseCase() {

    operator fun invoke(id: Long) = topicsRepository.getTopicById(id)

}