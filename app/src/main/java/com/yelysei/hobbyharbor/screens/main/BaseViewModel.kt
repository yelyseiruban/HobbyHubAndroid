package com.yelysei.hobbyharbor.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yelysei.hobbyharbor.model.results.Result

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>

