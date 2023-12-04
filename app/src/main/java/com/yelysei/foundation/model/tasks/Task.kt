package com.yelysei.foundation.model.tasks

import com.yelysei.foundation.model.FinalResult
import com.yelysei.foundation.model.tasks.dispatchers.Dispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originException: Exception? = null
) : Exception(originException)

interface Task<T> {

    fun await(): T

    /**
     * Non-blocking method for listening task results.
     * If task is cancelled before finishing, listener is not called.
     *
     * Listener is called in main thread.
     * @throws [IllegalStateException] if task has been already executed.
     */
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    fun cancel()

}