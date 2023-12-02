package com.yelysei.foundation

import com.yelysei.foundation.model.Repository

interface BaseApplication {
    val repositories: List<Repository>
}