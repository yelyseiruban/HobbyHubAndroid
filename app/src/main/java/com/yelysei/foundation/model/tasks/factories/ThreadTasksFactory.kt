package com.yelysei.foundation.model.tasks.factories

import android.os.Handler
import android.os.Looper
import com.yelysei.foundation.model.tasks.AbstractTask
import com.yelysei.foundation.model.tasks.SynchronizedTask
import com.yelysei.foundation.model.tasks.Task
import com.yelysei.foundation.model.tasks.TaskBody
import com.yelysei.foundation.model.tasks.TaskListener


private val handler = Handler(Looper.getMainLooper())

class ThreadTaskFactory : TasksFactory {
    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T> (
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            thread = Thread {
                executeBody(body, listener)
            }
            thread?.start()
        }

        override fun doCancel() {
            thread?.interrupt()
        }
    }
}