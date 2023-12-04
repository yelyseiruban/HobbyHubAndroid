package com.yelysei.foundation.model.tasks.factories

import com.yelysei.foundation.model.tasks.Task
import com.yelysei.foundation.model.tasks.TaskBody

interface TasksFactory {

    fun <T> async(body: TaskBody<T>): Task<T>

}