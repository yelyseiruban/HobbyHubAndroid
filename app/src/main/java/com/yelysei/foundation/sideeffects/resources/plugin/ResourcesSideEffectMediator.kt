package com.yelysei.foundation.sideeffects.resources.plugin

import android.content.Context
import com.yelysei.foundation.sideeffects.SideEffectMediator
import com.yelysei.foundation.sideeffects.resources.Resources

class ResourcesSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Resources {

    override fun getString(resId: Int, vararg args: Any): String {
        return appContext.getString(resId, *args)
    }

}