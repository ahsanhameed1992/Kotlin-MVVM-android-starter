package com.task.starter.usecase.errors

import com.task.starter.data.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
