package com.yelysei.hobbyharbor.app.model.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class IoDispatcher (
    val value: CoroutineDispatcher = Dispatchers.IO
)