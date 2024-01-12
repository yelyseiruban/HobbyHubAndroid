package com.yelysei.hobbyhub.utils.resources

import android.content.Context

class StringResources(
    private val context: Context
) {
    fun getString(id: Int, vararg formatArgs: String): String {
        return context.getString(id, *formatArgs)
    }
}