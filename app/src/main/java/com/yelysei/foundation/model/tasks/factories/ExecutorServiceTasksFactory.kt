package com.yelysei.foundation.model.tasks.factories

import com.yelysei.foundation.model.tasks.AbstractTask
import com.yelysei.foundation.model.tasks.SynchronizedTask
import com.yelysei.foundation.model.tasks.Task
import com.yelysei.foundation.model.tasks.TaskBody
import com.yelysei.foundation.model.tasks.TaskListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class ExecutorServiceTasksFactory(
    private val executorService: ExecutorService
) : TasksFactory {
    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ExecutorServiceTask(body))
    }

    private inner class ExecutorServiceTask<T> (
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var future: Future<*>? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            future = executorService.submit {
                executeBody(body, listener)
            }

        }

        override fun doCancel() {
            future?.cancel(true)
        }
    }
}