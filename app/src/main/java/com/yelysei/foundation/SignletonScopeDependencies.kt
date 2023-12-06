package com.yelysei.foundation

import android.content.Context
import androidx.annotation.MainThread

typealias SingletonsFactory = (applicationContext: Context) -> List<Any>
object SingletonScopeDependencies {

    private var factory: SingletonsFactory? = null
    private var dependencies: List<Any>? = null

    @MainThread
    fun init(factory: SingletonsFactory){
        if (this.factory == null) {
            this.factory = factory
        }
    }

    @MainThread
    fun getSingletonScopeDependencies(applicationContext: Context): List<Any> {
        val factory = this.factory
            ?: throw IllegalStateException("Call init() before getting singleton dependencies")
        return dependencies ?: factory(applicationContext).also { this.dependencies = it }
    }
}