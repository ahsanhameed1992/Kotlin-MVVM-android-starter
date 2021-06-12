package com.task.starter


sealed class DataStatus {
    object Success : DataStatus()
    object Fail : DataStatus()
    object EmptyResponse : DataStatus()
}
