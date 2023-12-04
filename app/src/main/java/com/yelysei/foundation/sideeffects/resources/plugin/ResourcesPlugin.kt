package com.yelysei.foundation.sideeffects.resources.plugin

import android.content.Context
import com.yelysei.foundation.sideeffects.SideEffectMediator
import com.yelysei.foundation.sideeffects.SideEffectPlugin


/**
 * Plugin for accessing app resources from view-models.
 * Allows adding [Resources] interface to the view-model constructor.
 */
class ResourcesPlugin : SideEffectPlugin<ResourcesSideEffectMediator, Nothing> {

    override val mediatorClass: Class<ResourcesSideEffectMediator>
        get() = ResourcesSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return ResourcesSideEffectMediator(applicationContext)
    }

}