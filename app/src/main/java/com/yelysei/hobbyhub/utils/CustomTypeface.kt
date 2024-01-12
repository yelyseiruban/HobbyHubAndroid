package com.yelysei.hobbyhub.utils


object CustomTypeface {
    fun capitalizeEachWord(text: String): CharSequence {
        val words = text.split(' ')
        return words.joinToString(separator = " ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
    }
}