package com.nexora.core

import android.util.Log

object NexoraLogger {
    private const val TAG = "NEXORA"
    
    fun info(tag: String, message: String) {
        Log.i("$TAG:$tag", message)
    }
    
    fun warn(tag: String, message: String) {
        Log.w("$TAG:$tag", message)
    }
    
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$TAG:$tag", message, throwable)
    }
    
    fun security(tag: String, message: String) {
        Log.println(Log.ERROR, "$TAG:SECURITY:$tag", message)
    }
    
    fun debug(tag: String, message: String) {
        Log.d("$TAG:$tag", message)
    }
}
