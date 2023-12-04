package com.yelysei.foundation.model.dispatchers

class ImmediateDispatcher : Dispatcher {
    override fun dispatch(block: () -> Unit) {
        block()
    }
}